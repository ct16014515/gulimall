package com.iflytek.gulimall.cart.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

@Configuration
public class TaskExecutorConfiguration {


    @Bean(name="executor")
    public TaskExecutor threadPoolTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(6);//核心线程数
        executor.setMaxPoolSize(12);//最大线程数
        executor.setKeepAliveSeconds(50);//线程最大空闲时间
        executor.setQueueCapacity(200);//缓存队列
        executor.setThreadNamePrefix("异步线程---");//设置属性,方便查看
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.DiscardPolicy());//如果线程池满了会丢掉这个任务不会抛异常
        executor.initialize();
        return executor;
    }

}
