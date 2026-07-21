package com.know.knowboot.redisson.service;

import org.redisson.api.RBucket;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.StringCodec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@ConditionalOnProperty(name = "know.redisson.enabled", havingValue = "true", matchIfMissing = false)
public class RedissonService {

    /**
     * 默认缓存时间
     */
    private static final Long DEFAULT_EXPIRED = 5 * 60L;
    /**
     * redis key前缀
     */
    private static final String REDIS_KEY_PREFIX = "";

    @Autowired
    private RedissonClient redissonClient;

    /**
     * 加锁
     *
     * @param lockKey
     * @return
     */
    public RLock lock(String lockKey) {
        RLock lock = redissonClient.getLock(lockKey);
        lock.lock();
        return lock;
    }

    /**
     * 带超时的锁
     *
     * @param lockKey
     * @param timeout 超时时间 单位：秒
     */
    public RLock lock(String lockKey, long timeout) {
        RLock lock = redissonClient.getLock(lockKey);
        lock.lock(timeout, TimeUnit.SECONDS);
        return lock;
    }

    /**
     * 带超时的锁
     *
     * @param lockKey
     * @param unit    时间单位
     * @param timeout 超时时间
     */
    public RLock lock(String lockKey, TimeUnit unit, long timeout) {
        RLock lock = redissonClient.getLock(lockKey);
        lock.lock(timeout, unit);
        return lock;
    }

    /**
     * 尝试获取锁
     *
     * @param lockKey
     * @param waitTime 最多等待时间
     * @param unit     TimeUnit时间单位
     * @return
     */
    public boolean tryLock(String lockKey, long waitTime, TimeUnit unit) {
        RLock lock = redissonClient.getLock(lockKey);
        try {
            return lock.tryLock(waitTime, unit);
        } catch (InterruptedException e) {
            return false;
        }
    }

    /**
     * 尝试获取锁
     *
     * @param lockKey
     * @param waitTime  最多等待时间
     * @param leaseTime 上锁后自动释放锁时间
     * @return
     */
    public boolean tryLock(String lockKey, long waitTime, long leaseTime) {
        RLock lock = redissonClient.getLock(lockKey);
        try {
            return lock.tryLock(waitTime, leaseTime, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            return false;
        }
    }

    /**
     * 尝试获取锁
     *
     * @param lockKey
     * @param unit      时间单位
     * @param waitTime  最多等待时间
     * @param leaseTime 上锁后自动释放锁时间
     * @return
     */
    public boolean tryLock(String lockKey, TimeUnit unit, long waitTime, long leaseTime) {
        RLock lock = redissonClient.getLock(lockKey);
        try {
            return lock.tryLock(waitTime, leaseTime, unit);
        } catch (InterruptedException e) {
            return false;
        }
    }

    /**
     * 释放锁
     *
     * @param lockKey
     */
    public void unlock(String lockKey) {
        RLock lock = redissonClient.getLock(lockKey);
        lock.unlock();
    }

    /**
     * 若没用锁情况下，就不调用释放锁的代码，若有锁情况下才调用释放锁
     *
     * @param lockKey
     */
    public void unlockIgnore(String lockKey) {
        RLock lock = redissonClient.getLock(lockKey);
        if (!lock.isLocked()) {
            return;
        }
        lock.unlock();
    }


    /**
     * 释放锁
     *
     * @param lock
     */
    public void unlock(RLock lock) {
        lock.unlock();
    }


    /**
     * 读取缓存
     *
     * @param key 缓存key
     * @param <T>
     * @return 缓存返回值
     */
    public <T> T get(String key) {
        RBucket<T> bucket = redissonClient.getBucket(REDIS_KEY_PREFIX + key);
        return bucket.get();
    }

    /**
     * 以string的方式读取缓存
     *
     * @param key 缓存key
     * @return 缓存返回值
     */
    public String getString(String key) {
        RBucket<String> bucket = redissonClient.getBucket(REDIS_KEY_PREFIX + key, StringCodec.INSTANCE);
        return bucket.get();
    }

    /**
     * 设置缓存（注：redisson会自动选择序列化反序列化方式）
     *
     * @param key   缓存key
     * @param value 缓存值
     * @param <T>
     */
    public <T> void put(String key, T value) {
        RBucket<T> bucket = redissonClient.getBucket(REDIS_KEY_PREFIX + key);
        bucket.set(value, DEFAULT_EXPIRED, TimeUnit.SECONDS);
    }

    /**
     * 以string的方式设置缓存
     *
     * @param key
     * @param value
     */
    public void putString(String key, String value) {
        RBucket<String> bucket = redissonClient.getBucket(REDIS_KEY_PREFIX + key, StringCodec.INSTANCE);
        bucket.set(value, DEFAULT_EXPIRED, TimeUnit.SECONDS);
    }

    /**
     * 以string的方式保存缓存（与其他应用共用redis时需要使用该函数）
     *
     * @param key     缓存key
     * @param value   缓存值
     * @param expired 缓存过期时间
     */
    public void putString(String key, String value, long expired) {
        RBucket<String> bucket = redissonClient.getBucket(REDIS_KEY_PREFIX + key, StringCodec.INSTANCE);
        bucket.set(value, expired <= 0 ? DEFAULT_EXPIRED : expired, TimeUnit.SECONDS);
    }

    /**
     * 如果不存在则写入缓存（string方式，不带有redisson的格式信息）
     *
     * @param key     缓存key
     * @param value   缓存值
     * @param expired 缓存过期时间
     */
    public boolean putStringIfAbsent(String key, String value, long expired) {
        RBucket<String> bucket = redissonClient.getBucket(REDIS_KEY_PREFIX + key, StringCodec.INSTANCE);
        return bucket.trySet(value, expired <= 0 ? DEFAULT_EXPIRED : expired, TimeUnit.SECONDS);
    }

    /**
     * 如果不存在则写入缓存（string方式，不带有redisson的格式信息）（不带过期时间，永久保存）
     *
     * @param key   缓存key
     * @param value 缓存值
     */
    public boolean putStringIfAbsent(String key, String value) {
        RBucket<String> bucket = redissonClient.getBucket(REDIS_KEY_PREFIX + key, StringCodec.INSTANCE);
        return bucket.trySet(value);
    }

    /**
     * 设置缓存
     *
     * @param key     缓存key
     * @param value   缓存值
     * @param expired 缓存过期时间
     * @param <T>     类型
     */
    public <T> void put(String key, T value, long expired) {
        RBucket<T> bucket = redissonClient.getBucket(REDIS_KEY_PREFIX + key);
        bucket.set(value, expired <= 0 ? DEFAULT_EXPIRED : expired, TimeUnit.SECONDS);
    }

    /**
     * 移除缓存
     *
     * @param key
     */
    public void remove(String key) {
        redissonClient.getBucket(REDIS_KEY_PREFIX + key).delete();
    }

    /**
     * 判断缓存是否存在
     *
     * @param key
     * @return
     */
    public boolean exists(String key) {
        return redissonClient.getBucket(REDIS_KEY_PREFIX + key).isExists();
    }


}
