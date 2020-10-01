package com.estsoft.pilot.app.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Create by madorik on 2020-09-28
 */
@Component
@Data
@ConfigurationProperties(prefix = "app")
public class PilotProperties {
    @Value("${app.file.dir}")
    private String fileDir;

    @Value("app.default.usermail")
    private String defaultUserEmail;
}
