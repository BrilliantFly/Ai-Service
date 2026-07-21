package com.know.knowboot.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.rabbit.config.RetryInterceptorBuilder;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.retry.RejectAndDontRequeueRecoverer;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.RetryListener;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

// 常用的三个配置如下
// 1---设置手动应答（acknowledge-mode: manual）
// 2---设置生产者消息发送的确认回调机制 (  #这个配置是保证提供者确保消息推送到交换机中，不管成不成功，都会回调
//    publisher-confirm-type: correlated
//    #保证交换机能把消息推送到队列中
//    publisher-returns: true
//     template:
//      #以下是rabbitmqTemplate配置
//      mandatory: true)
// 3---设置重试
@Slf4j
@Configuration
public class RabbitConfig {

    @Autowired
    private ConnectionFactory rabbitConnectionFactory;

    //@Bean  缓存连接池
    //public CachingConnectionFactory rabbitConnectionFactory

    @Autowired
    private RabbitProperties properties;

    //这里因为使用自动配置的connectionFactory，所以把自定义的connectionFactory注解掉
    // 存在此名字的bean 自带的连接工厂会不加载（也就是说yml中rabbitmq下一级不生效），如果想自定义来区分开 需要改变bean 的名称
//    @Bean
//    public ConnectionFactory connectionFactory() throws Exception {
//        //创建工厂类
//        CachingConnectionFactory cachingConnectionFactory=new CachingConnectionFactory();
//        //用户名
//        cachingConnectionFactory.setUsername("gust");
//        //密码
//        cachingConnectionFactory.setPassword("gust");
//        //rabbitMQ地址
//        cachingConnectionFactory.setHost("127.0.0.1");
//        //rabbitMQ端口
//        cachingConnectionFactory.setPort(Integer.parseInt("5672"));
//
//        //设置发布消息后回调
//        cachingConnectionFactory.setPublisherReturns(true);
//        //设置发布后确认类型，此处确认类型为交互
//        cachingConnectionFactory.setPublisherConfirmType(CachingConnectionFactory.ConfirmType.CORRELATED);
//
//        cachingConnectionFactory.setCacheMode(CachingConnectionFactory.CacheMode.CHANNEL);
//        return  cachingConnectionFactory;
//    }


    // 存在此名字的bean 自带的容器工厂会不加载（yml下rabbitmq下的listener下的simple配置），如果想自定义来区分开 需要改变bean 的名称
    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory() {
        SimpleRabbitListenerContainerFactory containerFactory = new SimpleRabbitListenerContainerFactory();
        containerFactory.setConnectionFactory(rabbitConnectionFactory);

        // 并发消费者数量
        containerFactory.setConcurrentConsumers(1);
        containerFactory.setMaxConcurrentConsumers(20);
        // 预加载消息数量 -- QOS
        containerFactory.setPrefetchCount(1);
        // 应答模式（此处设置为手动）
        containerFactory.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        //消息序列化方式
        containerFactory.setMessageConverter(new Jackson2JsonMessageConverter());
        // 设置通知调用链 （这里设置的是重试机制的调用链）
        containerFactory.setAdviceChain(
                RetryInterceptorBuilder
                        .stateless()
                        .recoverer(new RejectAndDontRequeueRecoverer())
                        .retryOperations(rabbitRetryTemplate())
                        .build()
        );
        return containerFactory;
    }

    // 存在此名字的bean 自带的容器工厂会不加载（yml下rabbitmq下的template的配置），如果想自定义来区分开 需要改变bean 的名称
    @Bean
    public RabbitTemplate rabbitTemplate() {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(rabbitConnectionFactory);
        //默认是用jdk序列化
        //数据转换为json存入消息队列，方便可视化界面查看消息数据
        rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
        //设置开启Mandatory,才能触发回调函数,无论消息推送结果怎么样都强制调用回调函数
        rabbitTemplate.setMandatory(true);

        /**
         *
         * 此处设置重试template后，会再生产者发送消息的时候，调用该template中的调用链
         *
         * 即：rabbitmq推送消息时候，会执行调用链
         *
         */
        rabbitTemplate.setRetryTemplate(rabbitRetryTemplate());

        /**
         * correlationData  消息唯一标识, 存在的意义便是：如果消息发送失败，可以根据这个标识补发消息
         * ack 交换机是否成功收到消息 true成功  false失败
         * cause 失败原因
         */
        rabbitTemplate.setConfirmCallback(
                (correlationData, ack, cause) -> {
                    log.info("--------------------- rabbitmq - ConfirmCallback ----------------------- 相关数据 -> {}",correlationData);
                    log.info("--------------------- rabbitmq - ConfirmCallback ----------------------- 确认情况 -> {}",ack);
                    log.info("--------------------- rabbitmq - ConfirmCallback ----------------------- 原因 -> {}",cause);
                });

        /**
         * ReturnCallback是获取未进入队列的回调信息，如果成功进入队列，不会进入这个回调方法。
         * 注意要触发ReturnCallback，必须设置rabbitTemplate.setMandatory(true);
         */
        rabbitTemplate.setReturnCallback((message, replyCode, replyText, exchange, routingKey) -> {
            log.info("--------------------- rabbitmq - ReturnCallback ----------------------- 消息 -> {}",message);
            log.info("--------------------- rabbitmq - ReturnCallback ----------------------- 回应码 -> {}",replyCode);
            log.info("--------------------- rabbitmq - ReturnCallback ----------------------- 回应消息 -> {}",replyText);
            log.info("--------------------- rabbitmq - ReturnCallback ----------------------- 交换机 -> {}",exchange);
            log.info("--------------------- rabbitmq - ReturnCallback ----------------------- 路由键 -> {}",routingKey);
        });

        return rabbitTemplate;
    }

    //重试的Template
    @Bean
    public RetryTemplate rabbitRetryTemplate() {
        RetryTemplate retryTemplate = new RetryTemplate();
        // 设置监听  调用重试处理过程
        retryTemplate.registerListener(new RetryListener() {
            @Override
            public <T, E extends Throwable> boolean open(RetryContext retryContext, RetryCallback<T, E> retryCallback) {
                // 执行之前调用 （返回false时会终止执行）
                log.info("--------------------- rabbitmq - 执行之前调用 -----------------------");
                return true;
            }

            @Override
            public <T, E extends Throwable> void close(RetryContext retryContext, RetryCallback<T, E> retryCallback, Throwable throwable) {
                // 重试结束的时候调用 （最后一次重试 ）
                log.info("--------------------- rabbitmq - 最后一次调用 -----------------------");

                return;
            }

            @Override
            public <T, E extends Throwable> void onError(RetryContext retryContext, RetryCallback<T, E> retryCallback, Throwable throwable) {
                //  异常 都会调用
                log.info("--------------------- rabbitmq - 第{}次调用 -----------------------",retryContext.getRetryCount());
            }
        });
        retryTemplate.setBackOffPolicy(backOffPolicyByProperties());
        retryTemplate.setRetryPolicy(retryPolicyByProperties());
        return retryTemplate;
    }

    @Bean
    public ExponentialBackOffPolicy backOffPolicyByProperties() {
        ExponentialBackOffPolicy backOffPolicy = new ExponentialBackOffPolicy();
        long maxInterval = properties.getListener().getSimple().getRetry().getMaxInterval().getSeconds();
        long initialInterval = properties.getListener().getSimple().getRetry().getInitialInterval().getSeconds();
        double multiplier = properties.getListener().getSimple().getRetry().getMultiplier();
        // 重试间隔
        backOffPolicy.setInitialInterval(initialInterval * 1000);
        // 重试最大间隔
        backOffPolicy.setMaxInterval(maxInterval * 1000);
        // 重试间隔乘法策略
        backOffPolicy.setMultiplier(multiplier);
        return backOffPolicy;
    }

    @Bean
    public SimpleRetryPolicy retryPolicyByProperties() {
        SimpleRetryPolicy retryPolicy = new SimpleRetryPolicy();
        int maxAttempts = properties.getListener().getSimple().getRetry().getMaxAttempts();
        retryPolicy.setMaxAttempts(maxAttempts);
        return retryPolicy;
    }

}
