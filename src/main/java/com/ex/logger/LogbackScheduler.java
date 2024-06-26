package com.ex.logger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
public class LogbackScheduler {

    private static int index = 1;
    private static final Logger classLogger = LoggerFactory.getLogger(LogbackScheduler.class);
    private static final Logger logger = LoggerFactory.getLogger("com.ex.logger.scheduling");

    @Scheduled(fixedRate = 5000) // 5초씩 스케줄링
    public void writeLog() {
        classLogger.info("안뇽하세요 {}", index);

        if(index % 2 == 0) {
            logger.info("{}번째 로그입니다.", index); // 실제 저장되는 로그
        }

        index++;
    }
}
