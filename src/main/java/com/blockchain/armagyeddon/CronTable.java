package com.blockchain.armagyeddon;

// @Scheduled이 명시된 메서드는 아규먼트를 가질 수 없고 반환타입은 void이어야 한다.

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class CronTable {

    // 매일 5시 30분 0초에 실행한다.
    @Scheduled(cron = "0 30 5 * * *")
    public void aJob() {

        // 실행될 로직
    }

    // 매월 1일 0시 0분 0초에 실행한다.
    @Scheduled(cron = "0 0 0 1 * *")
    public void anotherJob() {

        // 실행될 로직
    }

    // 애플리케이션 시작 후 60초 후에 첫 실행, 그 후 매 60초마다 주기적으로 실행한다.
    @Scheduled(initialDelay = 60000, fixedDelay = 60000)
    public void otherJob() {

        // 실행될 로직
    }
}
