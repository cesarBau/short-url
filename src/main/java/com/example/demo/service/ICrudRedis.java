package com.example.demo.service;

public interface ICrudRedis {

    void save(String key, String value, long expireAt);

    String find(String key);

    void delete(String key);

}
