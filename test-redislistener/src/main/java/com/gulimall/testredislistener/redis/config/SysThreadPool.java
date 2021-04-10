package com.gulimall.testredislistener.redis.config;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.lang.reflect.Method;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;


/**
 * 配置线程池
 *
 *
 * @author yuanyao@wistronits.com
 * create on 2019/1/23 17:15
 */
@Slf4j
@Data
@EnableAsync
@Configuration
@ConfigurationProperties(prefix = "thread.pool")
public class SysThreadPool implements AsyncConfigurer {

    /**
     * 表示线程池核心线程，正常情况下开启的线程数量
     */
    private Integer corePoolSize = 50;

    /**
     * 如果queueCapacity存满了，还有任务就会启动更多的线程，
     * 直到线程数达到maxPoolSize。如果还有任务，则根据拒绝策略进行处理
     */
    private Integer maxPoolSize = 100;

    /**
     * 当核心线程都在跑任务，还有多余的任务会存到此处
     */
    private Integer queueCapacity = 100;

    private Integer keepAliveSeconds = 60;

    private String threadNamePrefix = "filink-thread-pool-sys";

    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        //核心线程池大小
        executor.setCorePoolSize(corePoolSize);
        //最大线程数
        executor.setMaxPoolSize(maxPoolSize);
        //队列容量
        executor.setQueueCapacity(queueCapacity);
        //活跃时间
        executor.setKeepAliveSeconds(keepAliveSeconds);
        //线程名字前缀
        executor.setThreadNamePrefix(threadNamePrefix);

        // setRejectedExecutionHandler：当pool已经达到max size的时候，如何处理新任务
        // CallerRunsPolicy：不在新线程中执行任务，而是由调用者所在的线程来执行
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();
        return executor;
    }
    /**
     *  异步任务中异常处理
     * @return
     */
    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return new AsyncUncaughtExceptionHandler() {
            @Override
            public void handleUncaughtException(Throwable throwable, Method method, Object... objects) {
                log.info("==================线程发生错误=====================");
                log.error("=========================="+throwable.getMessage()+"=======================", throwable);
                log.error("exception method:"+method.getName());
            }
        };
    }
}
