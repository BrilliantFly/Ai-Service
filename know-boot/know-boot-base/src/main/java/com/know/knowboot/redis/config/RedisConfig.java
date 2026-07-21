//package com.know.knowboot.redis.config;
//
//import com.alibaba.fastjson.JSON;
//import com.fasterxml.jackson.annotation.JsonAutoDetect;
//import com.fasterxml.jackson.annotation.PropertyAccessor;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.databind.SerializationFeature;
//import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
//import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
//import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
//import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
//import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
//import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
//import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
//import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
//import com.know.knowboot.redis.manager.TtlRedisCacheManager;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.cache.interceptor.KeyGenerator;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Primary;
//import org.springframework.data.redis.cache.RedisCacheConfiguration;
//import org.springframework.data.redis.cache.RedisCacheManager;
//import org.springframework.data.redis.cache.RedisCacheWriter;
//import org.springframework.data.redis.connection.RedisConnectionFactory;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.data.redis.core.StringRedisTemplate;
//import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
//import org.springframework.data.redis.serializer.RedisSerializationContext;
//import org.springframework.data.redis.serializer.RedisSerializer;
//import org.springframework.data.redis.serializer.StringRedisSerializer;
//import org.springframework.util.DigestUtils;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.lang.reflect.Method;
//import java.nio.charset.StandardCharsets;
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.time.LocalTime;
//import java.time.format.DateTimeFormatter;
//
///**
// *  缓存雪崩：同一时刻redis的key全部失效。
// *  解决方案：给redis的时候有效期设置成随机。
//
// *  缓存击穿：同一时刻缓存没有数据 很多请求落DB
// *  解决方案：第一次请求如果没有数据 直接返回空。然后发一个消息队列 去同步DB。第二次再从缓存取数据。
// *
// *  因为redis是默认是惰性删除（key过期不会删除，取key的时候会看是否有效，如果没效则删除，所以会残留很多过期的key）
// */
//@Slf4j
//@Configuration
//public class RedisConfig {
//
//    @Bean
//    public KeyGenerator keyGenerator() {
//        return new KeyGenerator() {
//            @Override
//            public Object generate(Object target, Method method, Object... params) {
//                StringBuilder sb = new StringBuilder();
//                sb.append(target.getClass().getName());
//                sb.append(":");
//                sb.append(method.getName());
//                for (Object obj : params) {
//                    if (obj != null) {
//                        // 解决：HttpServletRequest、HttpServletResponse出现的异常It is illegal to call this method if the current request is not in asynchron
//                        if (!(obj instanceof HttpServletRequest) && !(obj instanceof HttpServletResponse)){
//                            sb.append(":");
//                            sb.append(obj.getClass().getName());
//                            sb.append(":");
//                            sb.append(JSON.toJSONString(obj));
//                        }
//                    }
//                }
//                log.info("要加密的字符串：{}", sb);
//                String md5DigestAsHex = DigestUtils.md5DigestAsHex(sb.toString().getBytes(StandardCharsets.UTF_8));
//                log.info("计算得到的缓存的key: {}", md5DigestAsHex);
//                return md5DigestAsHex;
//            }
//        };
//    }
//
//    /**
//     * redis序列化设置
//     * @param redisConnectionFactory redis连接工厂
//     * @return
//     */
//    @Bean
//    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
//        RedisTemplate<String, Object> template = new RedisTemplate<>();
//
//        template.setConnectionFactory(redisConnectionFactory);
//
//        //使用Jackson2JsonRedisSerializer来序列化和反序列化redis的value值（默认使用JDK的序列化方式）
//        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
//        ObjectMapper obm=new ObjectMapper();
//        // 指定要序列化的域，field,get和set,以及修饰符范围，ANY是都有包括private和publi
//        obm.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
//        // 指定序列化输入的类型，类必须是非final修饰的，final修饰的类，比如String,Integer等会跑出异常
//        obm.activateDefaultTyping(LaissezFaireSubTypeValidator.instance,ObjectMapper.DefaultTyping.NON_FINAL);
//        jackson2JsonRedisSerializer.setObjectMapper(obm);
//        //value
//        template.setValueSerializer(jackson2JsonRedisSerializer);
//
//        // key的序列化采用StringRedisSerializer
//        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
//        //key 采用String的序列化方式
//        template.setKeySerializer(stringRedisSerializer);
//        //hash
//        template.setHashKeySerializer(stringRedisSerializer);
//
//        template.afterPropertiesSet();
//        return template;
//    }
//
//    @Primary
//    @Bean
//    public RedisCacheManager redisCacheManager(RedisTemplate redisTemplate) {
//        RedisCacheWriter redisCacheWriter = RedisCacheWriter.nonLockingRedisCacheWriter(redisTemplate.getConnectionFactory());
//        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
//                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(redisTemplate.getValueSerializer()));
//        return new RedisCacheManager(redisCacheWriter, redisCacheConfiguration);
//    }
//
//    /**
//     * 自定义RedisCacheManager，用于在使用@Cacheable时设置ttl
//     * 使用@Cacheable时，无法直接设置过期时间，需要自定义RedisCacheManager来实现ttl设置
//     * @Cacheable(value = "p_user=600"，key = "#menu+'_'+#type+'_'+#userId"，cacheManager = "selfCacheManager", unless = "#result == null"）
//     * 使用时在value/cacheNames结尾拼接 =多少秒，
//     */
//    @Bean
//    public RedisCacheManager selfCacheManager(RedisTemplate redisTemplate) {
//        RedisCacheWriter redisCacheWriter = RedisCacheWriter.nonLockingRedisCacheWriter(redisTemplate.getConnectionFactory());
//        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
//                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(redisTemplate.getValueSerializer()));
//        return new TtlRedisCacheManager(redisCacheWriter, redisCacheConfiguration);
//    }
//
//
////    @Bean
////    public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory factory) {
////        StringRedisTemplate stringRedisTemplate = new StringRedisTemplate();
////        stringRedisTemplate.setConnectionFactory(factory);
////        return stringRedisTemplate;
////    }
////
////    /**
////     * key类型采用String序列化
////     *
////     * @return
////     */
////    private RedisSerializer<String> keySerializer() {
////        return new StringRedisSerializer();
////    }
////
////    /**
////     * value采用JSON序列化
////     *
////     * @return
////     */
////    private RedisSerializer<Object> valueSerializer() {
////        //设置jackson序列化
////        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(Object.class);
////        //设置序列化对象
////        jackson2JsonRedisSerializer.setObjectMapper(getMapper());
////        return jackson2JsonRedisSerializer;
////    }
////
////    /**
////     * 使用com.fasterxml.jackson.databind.ObjectMapper
////     * 对数据进行处理包括java8里的时间
////     *
////     * @return
////     */
////    private ObjectMapper getMapper() {
////        ObjectMapper mapper = new ObjectMapper();
////        //设置可见性
////        mapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
////        //默认键入对象
////        mapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
////        //设置Java 8 时间序列化
////        JavaTimeModule timeModule = new JavaTimeModule();
////        timeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
////        timeModule.addSerializer(LocalDate.class, new LocalDateSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
////        timeModule.addSerializer(LocalTime.class, new LocalTimeSerializer(DateTimeFormatter.ofPattern("HH:mm:ss")));
////        timeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
////        timeModule.addDeserializer(LocalDate.class, new LocalDateDeserializer(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
////        timeModule.addDeserializer(LocalTime.class, new LocalTimeDeserializer(DateTimeFormatter.ofPattern("HH:mm:ss")));
////        //禁用把时间转为时间戳
////        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
////        mapper.registerModule(timeModule);
////        return mapper;
////    }
//
//}
