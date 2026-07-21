package com.know.knowboot.tokenbucket.config;


import com.google.common.util.concurrent.RateLimiter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * 负责实现限速逻辑
 *
 * 创建一个限速器，每1秒，产生2.5个令牌
 * RateLimiter rateLimiter = RateLimiter.create(2.5, 1, TimeUnit.SECONDS);
 *
 * 尝试获取1个令牌，如果没有，会阻塞当前线程。直到获取成功返回。
 * 返回值是，阻塞的秒数
 *
 * 尝试获取1个令牌，不会阻塞当前线程。
 * 立即返回是否获取成功。
 * boolean success = rateLimiter.tryAcquire();
 */
@Configuration
public class RateLimiterConfig {

    @Value("${know.rateLimiter.all.permitsPerSecond}")
    private String allPermitsPerSecond;

    @Value("${know.rateLimiter.all.warmupPeriod}")
    private String allWarmupPeriod;

    @Value("${know.rateLimiter.resource.permitsPerSecond}")
    private String resourcePermitsPerSecond;

    @Value("${know.rateLimiter.resource.warmupPeriod}")
    private String resourceWarmupPeriod;

    @Value("${know.rateLimiter.interface.permitsPerSecond}")
    private String interfacePermitsPerSecond;

    @Value("${know.rateLimiter.interface.warmupPeriod}")
    private String interfaceWarmupPeriod;

    /**
     *
     * 全局限流器
     *
     * 创建一个稳定输出令牌的RateLimiter，保证了平均每秒不超过qps个请求
     * 当请求到来的速度超过了qps，保证每秒只处理qps个请求
     * 当这个RateLimiter使用不足(即请求到来速度小于qps)，会囤积最多qps个请求
     *
     * 创建的是SmoothBursty 实例 平滑稳定
     */
    @Bean
    public RateLimiter rateLimiterAllSmoothBursty() {
        RateLimiter rateLimiter = RateLimiter.create(Double.parseDouble(allPermitsPerSecond));
        return rateLimiter;
    }

    /**
     *
     * 全局限流器
     *
     * 根据指定的稳定吞吐率和预热期来创建RateLimiter，
     * 这里的吞吐率是指每秒多少许可数（通常是指QPS，每秒多少查询），
     * 在这段预热时间内，RateLimiter每秒分配的许可数会平稳地增长直到预热期结束时达到其最大速率（只要存在足够请求数来使其饱和）。
     * 同样地，如果RateLimiter 在warmupPeriod时间内闲置不用，它将会逐步地返回冷却状态。
     * 也就是说，它会像它第一次被创建般经历同样的预热期。
     * 返回的RateLimiter 主要用于那些需要预热期的资源，这些资源实际上满足了请求（比如一个远程服务），
     * 而不是在稳定（最大）的速率下可以立即被访问的资源。
     * 返回的RateLimiter 在冷却状态下启动（即预热期将会紧跟着发生），并且如果被长期闲置不用，它将回到冷却状态
     *
     * RateLimiter.create(1, 1, TimeUnit.SECONDS)
     *
     * 创建的是SmoothWarmingUp实例    平滑预热
     */
    @Bean
    public RateLimiter rateLimiterAllSmoothWarmingUpRate() {
        RateLimiter rateLimiter = RateLimiter.create(Double.parseDouble(allPermitsPerSecond), Long.valueOf(allWarmupPeriod), TimeUnit.SECONDS);
        return rateLimiter;
    }

    /**
     * 针对单个resource限流器
     * @return
     */
    @Bean
    public RateLimiter rateLimiterResourceSmoothBursty() {
        RateLimiter rateLimiter = RateLimiter.create(Double.parseDouble(resourcePermitsPerSecond));
        return rateLimiter;
    }

    /**
     * 针对单个resource限流器
     * @return
     */
    @Bean
    public RateLimiter rateLimiterResourceSmoothWarmingUpRate() {
        RateLimiter rateLimiter = RateLimiter.create(Double.parseDouble(resourcePermitsPerSecond), Long.valueOf(resourceWarmupPeriod), TimeUnit.SECONDS);
        return rateLimiter;
    }

    /**
     * 针对单个接口限流器
     * @return
     */
    @Bean
    public RateLimiter rateLimiterInterfaceSmoothBursty() {
        RateLimiter rateLimiter = RateLimiter.create(Double.parseDouble(interfacePermitsPerSecond));
        return rateLimiter;
    }

    /**
     * 针对单个接口限流器
     * @return
     */
    @Bean
    public RateLimiter rateLimiterInterfaceSmoothWarmingUpRate() {
        RateLimiter rateLimiter = RateLimiter.create(Double.parseDouble(interfacePermitsPerSecond), Long.valueOf(interfaceWarmupPeriod), TimeUnit.SECONDS);
        return rateLimiter;
    }


}
