package com.blockchain.armagyeddon;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

@SpringBootApplication
@EnableScheduling // 작업 스케쥴러 활성화
public class ArmagyeddonApplication {

    public static void main(String[] args) {
        ApplicationContext ctx = SpringApplication.run(ArmagyeddonApplication.class, args);
    }

//    // 단일 쓰래드
//    @Bean
//    @Async // 여러 작업 동시에 실행할 때 비동기 처리
//    public TaskScheduler taskScheduler() {
//
//        return new ConcurrentTaskScheduler();
//    }

    // 쓰래드 풀
    @Bean
    public TaskScheduler taskScheduler() {

        ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
        taskScheduler.setPoolSize(10);

        return taskScheduler;
    }

}
