package com.zhang.reggie.config;

import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig extends CachingConfigurerSupport {

    @Bean
    public RedisTemplate<Object,Object> redisTemplate(RedisConnectionFactory connectionFactory){

        RedisTemplate<Object, Object> RedisTemplate = new RedisTemplate<>();
        //修改默认序列化
        RedisTemplate.setKeySerializer(new StringRedisSerializer());
        RedisTemplate.setConnectionFactory(connectionFactory);
        return RedisTemplate;
    }

}
