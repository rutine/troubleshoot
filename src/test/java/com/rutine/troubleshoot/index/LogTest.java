package com.rutine.troubleshoot.index;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

/**
 * @author rutine
 * @date 2020/1/15 13:51
 */
@SpringJUnitConfig
public class LogTest {

    @DisplayName("测试log")
    @RepeatedTest(10)
    public void testLog() {
        Logger logger = LoggerFactory.getLogger(LogTest.class);

        int threadCount = 50;
        int logCount = 1000000;
        for (int i = 0; i < threadCount; i++) {
            new Thread(() -> {
                String threadName = Thread.currentThread().getName();
                for (int j = 0; j < logCount; j++) {
                    logger.info("------> 测试log: {}", threadName);
                }
            }).start();
        }
    }
}
