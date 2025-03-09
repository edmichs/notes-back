package com.speer.notes.config.rateLimit;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.HandlerInterceptor;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RateLimitInterceptor implements HandlerInterceptor {
    private final Map<String, Bucket> buckets = new ConcurrentHashMap<>();
    private final boolean enabled;
    private final int rateLimit;
    private final int durationInSeconds;

    public RateLimitInterceptor(boolean enabled, int rateLimit, int durationInSeconds) {
        this.enabled = enabled;
        this.rateLimit = rateLimit;
        this.durationInSeconds = durationInSeconds;
    }


    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!enabled) {
            return true;
        }

        String ipAddress = getClientIpAddress(request);
        Bucket bucket = buckets.computeIfAbsent(ipAddress, this::createNewBucket);

        if (bucket.tryConsume(1)) {
            response.addHeader("X-Rate-Limit-Remaining", String.valueOf(bucket.getAvailableTokens()));
            return true;
        } else {
            response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            response.getWriter().write("Too many requests - please try again later");
            return false;
        }
    }

    private Bucket createNewBucket(String key) {
        Refill refill = Refill.intervally(rateLimit, Duration.ofSeconds(durationInSeconds));
        Bandwidth limit = Bandwidth.classic(rateLimit, refill);
        return Bucket.builder().addLimit(limit).build();
    }

    private String getClientIpAddress(HttpServletRequest request) {
        String xForwardedForHeader = request.getHeader("X-Forwarded-For");
        if (xForwardedForHeader != null && !xForwardedForHeader.isEmpty()) {
            return xForwardedForHeader.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }
}
