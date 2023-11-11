package com.yupi.springbootinit.config;

import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 自定义线程池
 * @author Rocky
 */
@Configuration
public class TreadPoolExecutorConfig {

    /**
     * 定义一个线程工厂
     */
    ThreadFactory threadFactory = new ThreadFactory() {
        // 每个定义线程号
        private int count = 1;

        @Override
        public Thread newThread(@NotNull Runnable r) {
            Thread thread = new Thread(r);
            thread.setName("线程" + count);
            count++;
            return thread;
        }
    };

    @Bean
    public ThreadPoolExecutor doExecutor() {
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(2, 4, 100, TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(4), threadFactory);
        return threadPoolExecutor;
    }

    // https://mp-37e138be-f1ed-4657-a505-1f71130432d8.cdn.bspapp.com/cloudstorage/33bfbc75-d25c-4265-9e3c-59d0f401af7a.jpeg
}
