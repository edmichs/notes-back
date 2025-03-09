package com.speer.notes.config.rateLimit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class RateLimitConfig implements WebMvcConfigurer {
    @Value("${app.ratelimiting.enabled:true}")
    private boolean rateLimitingEnabled;

    @Value("${app.ratelimiting.limit:100}")
    private int rateLimit;

    @Value("${app.ratelimiting.duration:60}")
    private int durationInSeconds;

    @Bean
    public RateLimitInterceptor rateLimitInterceptor() {
        return new RateLimitInterceptor(rateLimitingEnabled, rateLimit, durationInSeconds);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(rateLimitInterceptor())
                .addPathPatterns("/api/**")
                .excludePathPatterns("/api/auth/**", "/api-docs/**", "/swagger-ui/**");
    }
}
