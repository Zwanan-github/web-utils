package com.example.backend.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.StringRedisSerializer

@Configuration
class RedisConfig<T> {

    @Bean
    fun getRedisTemplate(factory: RedisConnectionFactory): RedisTemplate<String, T> {
        val template: RedisTemplate<String, T> = RedisTemplate();
        template.keySerializer = StringRedisSerializer();
        template.hashKeySerializer = StringRedisSerializer();
        template.hashValueSerializer = GenericJackson2JsonRedisSerializer();
        template.valueSerializer = GenericJackson2JsonRedisSerializer();
        template.setEnableTransactionSupport(true);
        template.connectionFactory = factory;
        return template;
    }

}