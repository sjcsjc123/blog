package com.example.myblog.config;

import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;

@EnableConfigurationProperties(CacheProperties.class)
@Configuration
@EnableCaching
public class RedisCatheConfig {

    @Bean
    public RedisCacheConfiguration redisCacheConfiguration(CacheProperties cacheProperties) {
        RedisCacheConfiguration config =
                RedisCacheConfiguration.defaultCacheConfig();
        config = config.serializeKeysWith(RedisSerializationContext.
                SerializationPair.fromSerializer(RedisSerializer.string()));
        config = config.serializeValuesWith(RedisSerializationContext.
                SerializationPair.fromSerializer(RedisSerializer.json()));
        CacheProperties.Redis cachePropertiesRedis = cacheProperties.getRedis();
        if (cachePropertiesRedis.getTimeToLive() != null){
            config = config.entryTtl(cachePropertiesRedis.getTimeToLive());
        }
        if (cachePropertiesRedis.getKeyPrefix() != null){
            config = config.prefixCacheNameWith(cachePropertiesRedis.getKeyPrefix());
        }
        if (!cachePropertiesRedis.isCacheNullValues()){
            config = config.disableCachingNullValues();
        }
        if(!cachePropertiesRedis.isUseKeyPrefix()){
            config = config.disableKeyPrefix();
        }
        return config;
    }

}
