package com.estsoft.pilot.app.config;

import com.estsoft.pilot.app.config.auth.BoardAuthInterceptor;
import com.estsoft.pilot.app.config.auth.CommentAuthInterceptor;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Create by madorik on 2020-10-03
 */
@AllArgsConstructor
@Configuration
public class WebServiceConfig implements WebMvcConfigurer {

    private final BoardAuthInterceptor boardAuthInterceptor;

    private final CommentAuthInterceptor commentAuthInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(boardAuthInterceptor)
                .excludePathPatterns("/api/v1/boards")
                .excludePathPatterns("/api/v1/boards/**/comments/**")
                .addPathPatterns("/api/v1/boards/**");

        registry.addInterceptor(commentAuthInterceptor)
                .excludePathPatterns("/api/v1/boards/**/comments")
                .addPathPatterns("/api/v1/boards/**/comments/**");

    }
}