package com.lp.configuration;

import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.*;

/**
 * @author LuoPing
 * @date 2022/10/15 11:20
 * 日志线程池配置
 */
@Configuration
public class ThreadPoolConfiguration {
    // 核心线程池大小
    private int corePoolSize = 50;

    // 最大可创建的线程数
    private int maxPoolSize = 200;

    // 队列最大长度
    private int queueCapacity = 1000;

    // 线程池维护线程所允许的空闲时间
    private int keepAliveSeconds = 300;
    @Bean
    public ExecutorService executorService(){
        return new ThreadPoolExecutor(corePoolSize,maxPoolSize,keepAliveSeconds,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(100),
                new BasicThreadFactory.Builder().namingPattern("lpclasslog-%d")
                        .daemon(true).build(),
                new ThreadPoolExecutor.AbortPolicy());

    }
}
