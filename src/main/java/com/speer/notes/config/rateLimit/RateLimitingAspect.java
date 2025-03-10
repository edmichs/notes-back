package com.speer.notes.config.rateLimit;

import io.github.resilience4j.ratelimiter.RateLimiter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class RateLimitingAspect {
    private final RateLimiter apiRateLimiter;
    private final RateLimiter authRateLimiter;

    @Around("execution(* com.speer.notes.controller.NoteController.*(..)) || " +
            "execution(* com.speer.notes.controller.SearchController.*(..))")
    public Object rateLimitApiEndpoints(ProceedingJoinPoint joinPoint) throws Throwable {
        return RateLimiter.decorateSupplier(apiRateLimiter, () -> {
            try {
                return joinPoint.proceed();
            } catch (Throwable throwable) {
                if (throwable instanceof RuntimeException) {
                    throw (RuntimeException) throwable;
                }
                throw new RuntimeException(throwable);
            }
        }).get();
    }

    @Around("execution(* com.speer.notes.controller.AuthController.*(..))")
    public Object rateLimitAuthEndpoints(ProceedingJoinPoint joinPoint) throws Throwable {
        return RateLimiter.decorateSupplier(authRateLimiter, () -> {
            try {
                return joinPoint.proceed();
            } catch (Throwable throwable) {
                if (throwable instanceof RuntimeException) {
                    throw (RuntimeException) throwable;
                }
                throw new RuntimeException(throwable);
            }
        }).get();
    }
}
