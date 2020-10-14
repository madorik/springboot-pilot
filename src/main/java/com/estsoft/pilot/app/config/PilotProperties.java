package com.estsoft.pilot.app.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Create by madorik on 2020-09-28
 */
@Getter
@Setter
@Configuration
//@PropertySources(@PropertySource(value = "file:D:\\application.properties"))
@ConfigurationProperties(prefix = "app", ignoreInvalidFields = true)
public class PilotProperties {

    private String fileDir;

    private String defaultUserEmail;
}
