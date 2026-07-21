package com.know.knowboot.redisson.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 *  1. 普通的可重入锁
 *     RLock lock = redissonClient.getLock("generalLock");
 *
 *     拿锁失败时会不停的重试
 *     具有Watch Dog 自动延期机制 默认续30s 每隔30/3=10 秒续到30s
 *     lock.lock();
 *
 *     尝试拿锁10s后停止重试,返回false
 *     具有Watch Dog 自动延期机制 默认续30s
 *     boolean res1 = lock.tryLock(10, TimeUnit.SECONDS);
 *
 *     拿锁失败时会不停的重试
 *     没有Watch Dog ，10s后自动释放
 *     lock.lock(10, TimeUnit.SECONDS);
 *
 *     尝试拿锁100s后停止重试,返回false
 *     没有Watch Dog ，10s后自动释放
 *     boolean res2 = lock.tryLock(100, 10, TimeUnit.SECONDS);
 *
 *  2. 公平锁 保证 Redisson 客户端线程将以其请求的顺序获得锁
 *     RLock fairLock = redissonClient.getFairLock("fairLock");
 *
 *  3. 读写锁 没错与JDK中ReentrantLock的读写锁效果一样
 *     RReadWriteLock readWriteLock = redissonClient.getReadWriteLock("readWriteLock");
 *     readWriteLock.readLock().lock();
 *     readWriteLock.writeLock().lock();
 *
 */
@Configuration
@ConditionalOnProperty(name = "know.redisson.enabled", havingValue = "true", matchIfMissing = false)
public class RedissonConfig {

    @Value("${know.redisson.address}")
    private String addressUrl;

    @Bean
    public RedissonClient redissonClient() throws Exception{
        RedissonClient redisson = null;
        Config config = new Config();
        config.useSingleServer()
                .setAddress(addressUrl);
        redisson = Redisson.create(config);
        return redisson;
    }

}
