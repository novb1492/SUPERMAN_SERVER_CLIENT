package com.kimcompany.jangbogbackendver2.Config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurerSupport;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync
public class AsyncConfig extends AsyncConfigurerSupport {

    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(100);//기본적으로 대기하고있는 thread개수
        executor.setMaxPoolSize(100);//동시 동작하는 최대 thread개수
        executor.setQueueCapacity(500);//MaxPoolSize초과시
        executor.setThreadNamePrefix("Superman-async-");//접두사
        executor.initialize();
        return executor;
    }
}
