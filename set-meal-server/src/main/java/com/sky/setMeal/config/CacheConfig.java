package com.sky.setMeal.config;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

@EnableCaching
@Configuration
public class CacheConfig {
    @Bean
    public RedisCacheConfiguration redisCacheConfiguration(Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer) {
        // 构建Redis缓存配置
        return RedisCacheConfiguration.defaultCacheConfig()
                // 设置缓存默认过期时间
                .entryTtl(Duration.ofHours(1))
                // 缓存Key序列化：使用String序列化
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                // 缓存Value序列化：使用JSON序列化
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(jackson2JsonRedisSerializer))
                // 禁用空值缓存，避免缓存null值导致的问题
                .disableCachingNullValues();
    }
}