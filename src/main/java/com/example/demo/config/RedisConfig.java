package com.example.demo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import redis.clients.jedis.Jedis;

@Configuration
@PropertySource("classpath:other.properties")
public class RedisConfig {

    @Value("${host.redis}")
    private String redisHost;

    @Value("${port.redis}")
    private Integer redisPort;

    @Bean
    Jedis clientJedis() {
        return new Jedis(redisHost, redisPort);
    }
}
