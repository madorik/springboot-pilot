package com.estsoft.pilot.app.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Create by madorik on 2020-09-28
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "app", ignoreInvalidFields = true)
public class PilotProperties {

    private String fileDir;

    private String defaultUserEmail;
}
