package com.sharding.conf.taskPool;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author : zhouhao
 * @version V1.0
 * @Project: ghs
 * @Description: TODO
 * @date Date : 2021年09月03日 下午2:43
 */
@Configuration
@Slf4j
public class ThreadPoolConfig {
    @Bean
    public ExecutorService getThreadPool() {
        log.info("初始化线程池，核心线程数:{}", Runtime.getRuntime().availableProcessors());
        return new ThreadPoolExecutor(Runtime.getRuntime().availableProcessors(), (Runtime.getRuntime().availableProcessors()) * 2, 20L, TimeUnit.SECONDS, new LinkedBlockingQueue<>(50));
    }
}
