//package com.know.knowboot.threadpool.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.task.TaskExecutor;
//import org.springframework.scheduling.annotation.EnableAsync;
//import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
//
//import java.util.concurrent.ThreadPoolExecutor;
//
//@Configuration
//// 开启异步任务
//@EnableAsync
//public class ThreadPoolConfig {
//
//    /**
//     *   每秒需要多少个线程处理?
//     *   tasks/(1/taskcost)
//     */
//    private int corePoolSize = 3;
//
//    /**
//     * 线程池维护线程的最大数量
//     * (max(tasks)- queueCapacity)/(1/taskcost)
//     */
//    private int maxPoolSize = 3;
//
//    /**
//     * 缓存队列
//     * (coreSizePool/taskcost)*responsetime
//     */
//    private int queueCapacity = 10;
//
//    /**
//     * 允许的空闲时间
//     * 默认为60
//     */
//    private int keepAlive = 100;
//
//    /**
//     * 默认线程名称前缀
//     */
//    private final String threadNamePrefix = "thread-conifg-";
//
//    @Bean(name = "taskExecutor")
//    public TaskExecutor taskExecutor() {
//        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
//        // 设置核心线程数
//        // 线程池创建的核心线程数，线程池维护线程的最少数量，即使没有任务需要执行，也会一直存活
//        executor.setCorePoolSize(corePoolSize);
//        // 设置最大线程数
//        // 最大线程池数量，当线程数>=corePoolSize，且任务队列已满时。线程池会创建新线程来处理任务
//        // 任务队列已满时, 且当线程数=maxPoolSize，，线程池会拒绝处理任务而抛出异常
//        executor.setMaxPoolSize(maxPoolSize);
//        // 设置队列容量
//        // 如果设置allowCoreThreadTimeout=true（默认false）时，核心线程会超时关闭
//        // executor.setAllowCoreThreadTimeOut(true);
//        // 阻塞队列 当核心线程数达到最大时，新任务会放在队列中排队等待执行
//        executor.setQueueCapacity(queueCapacity);
//        // 设置允许的空闲时间（秒）
//        // 当线程空闲时间达到keepAliveTime时，线程会退出，直到线程数量=corePoolSize
//        // 允许线程空闲时间30秒，当maxPoolSize的线程在空闲时间到达的时候销毁
//        // 如果allowCoreThreadTimeout=true，则会直到线程数量=0
////        executor.setKeepAliveSeconds(keepAlive);
//        // 设置默认线程名称
//        // spring 提供的 ThreadPoolTaskExecutor 线程池，是有setThreadNamePrefix() 方法的。
//        // jdk 提供的ThreadPoolExecutor 线程池是没有 setThreadNamePrefix() 方法的
//        executor.setThreadNamePrefix(threadNamePrefix);
//        // 设置拒绝策略rejection-policy：当pool已经达到max size的时候，如何处理新任务
//        // CALLER_RUNS：不在新线程中执行任务，而是有调用者所在的线程来执行
//        // rejection-policy：拒绝策略：当线程数已经达到maxSize的时候，如何处理新任务
//        // CallerRunsPolicy()：交由调用方线程运行，比如 main 线程；如果添加到线程池失败，那么主线程会自己去执行该任务，不会等待线程池中的线程去执行
//        // AbortPolicy()：该策略是线程池的默认策略，如果线程池队列满了丢掉这个任务并且抛出RejectedExecutionException异常。
//        // DiscardPolicy()：如果线程池队列满了，会直接丢掉这个任务并且不会有任何异常
//        // DiscardOldestPolicy()：丢弃队列中最老的任务，队列满了，会将最早进入队列的任务删掉腾出空间，再尝试加入队列
//        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
//        // 等待所有任务结束后再关闭线程池
//        executor.setWaitForTasksToCompleteOnShutdown(true);
//
//        return executor;
//    }
//
//}
