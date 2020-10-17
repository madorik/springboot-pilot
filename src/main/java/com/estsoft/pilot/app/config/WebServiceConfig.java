package com.estsoft.pilot.app.config;

import com.estsoft.pilot.app.config.auth.BoardAuthInterceptor;
import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.AllArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

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
            SimpleCacheManager cacheManager = new SimpleCacheManager();
            List<CaffeineCache> caches = Arrays.stream(CacheType.values())
                    .map(cache -> new CaffeineCache(cache.getCacheName(), Caffeine.newBuilder().recordStats()
                            .expireAfterWrite(cache.getExpiredAfterWrite(), TimeUnit.SECONDS)
                            .maximumSize(cache.getMaximumSize())
                            .build()))
                    .collect(Collectors.toList());
            cacheManager.setCaches(caches);
            return cacheManager;
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