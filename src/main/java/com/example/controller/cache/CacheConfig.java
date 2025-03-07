package com.example.controller.cache;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class CacheConfig {

    private final Cache<String, Long> cache;

    public CacheConfig() {
        this.cache = Caffeine.newBuilder()
                .expireAfterWrite(60, TimeUnit.SECONDS)
                .maximumSize(1000)
                .build();
    }

    public Cache<String, Long> getCache() {
        return cache;
    }
}