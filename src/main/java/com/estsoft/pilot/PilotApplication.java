package com.estsoft.pilot;

import com.estsoft.pilot.app.config.PilotProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * Create by madorik on 2020-09-20
 */
@EnableConfigurationProperties(value = { PilotProperties.class })
@EnableJpaAuditing
@SpringBootApplication
public class PilotApplication {

    public static void main(String[] args) {
        SpringApplication.run(PilotApplication.class, args);
    }
}
