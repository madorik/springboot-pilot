package com.estsoft.pilot.app.config;

import com.estsoft.pilot.app.config.auth.BoardAuthInterceptor;
import lombok.AllArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Objects;

/**
 * Create by madorik on 2020-10-03
 */
@AllArgsConstructor
@Configuration
public class WebServiceConfig implements WebMvcConfigurer {

    private final BoardAuthInterceptor boardAuthInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(boardAuthInterceptor)
                .excludePathPatterns("/api/v1/boards")
                .excludePathPatterns("/api/v1/boards/**/comments/**")
                .addPathPatterns("/api/v1/boards/**");
    }

    @Configuration
    public static class CustomCacheConfiguration extends CachingConfigurerSupport {

        @Bean
        public CacheManager cacheManager() {
            return new EhCacheCacheManager(Objects.requireNonNull(ehCacheCacheManager().getObject()));
        }

        @Bean
        public EhCacheManagerFactoryBean ehCacheCacheManager() {
            EhCacheManagerFactoryBean factory = new EhCacheManagerFactoryBean();
            factory.setConfigLocation(new ClassPathResource("ehcache.xml"));
            factory.setShared(true);
            return factory;
        }
    }
}