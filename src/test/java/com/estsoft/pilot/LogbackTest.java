package com.estsoft.pilot;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;


/**
 * Create by madorik on 2020-09-21
 */
@SpringBootTest
public class LogbackTest {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Test
    void log() {
        logger.info("[INFO]");
        logger.debug("[DEBUG]");
        logger.warn("[WARN]");
        logger.error("[ERROR]");
    }
}
