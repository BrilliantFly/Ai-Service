package com.know.knowboot.threadpool.config;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;

@Configuration
public class ThreadPoolExecutorConfig {

    private static ThreadFactory namedThreadFactory = new ThreadFactoryBuilder()
            .setNameFormat("COMMON-POOL-%d").build();

    //cpu数量
    private static final int CPU_NUM = Runtime.getRuntime().availableProcessors();
    //阻塞系数
    private static final double BLOCKING_COEFFICIENT = 0.5;
    //求得线程数大小
    private static final int POOL_SIZE = (int) (CPU_NUM / (1 - BLOCKING_COEFFICIENT));

    @Bean(name = "threadPoolExecutor")
    public Executor threadPoolExecutor() {
        ThreadPoolTaskExecutor threadPoolExecutor = new ThreadPoolTaskExecutor();
        // 核心池大小
        threadPoolExecutor.setCorePoolSize(POOL_SIZE);
        // 最大线程数
        threadPoolExecutor.setMaxPoolSize(2 * POOL_SIZE);
        // 线程空闲时间
        threadPoolExecutor.setKeepAliveSeconds(300);
        // 队列长度
        threadPoolExecutor.setQueueCapacity(2 * POOL_SIZE);
        threadPoolExecutor.setThreadFactory(namedThreadFactory);
        //拒绝策略
        threadPoolExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());
        //优先级
        threadPoolExecutor.setThreadPriority(Thread.MAX_PRIORITY);
        return threadPoolExecutor;
    }
}
