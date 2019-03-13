package com.rutine.troubleshoot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 *
 * @author rutine
 * @date 2019/3/12 18:32
 */
@SpringBootApplication
public class Troubleshoot {
    private static Logger logger = LoggerFactory.getLogger(Troubleshoot.class);

    /**
     * Main Start
     */
    public static void main(String[] args) {
        SpringApplication.run(Troubleshoot.class, args);
        logger.info("============= Troubleshoot Start Success =============");
    }
}
