package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.log4j.Log4j2;
import redis.clients.jedis.Jedis;

@Log4j2
@Service
public class RedisService implements ICrudRedis {

    @Autowired
    private final Jedis clientJedis;

    public RedisService(Jedis clientJedis) {
        this.clientJedis = clientJedis;
    }

    @Override
    public void save(String key, String value, long expireAt) {
        log.info("Consume service save");
        clientJedis.set(key, value);
        clientJedis.expire(key, expireAt);
    }

    @Override
    public String find(String key) {
        log.info("Consume service find");
        return clientJedis.get(key);
    }

    @Override
    public void delete(String key) {
        log.info("Consume service delete");
        clientJedis.del(key);
    }

}
