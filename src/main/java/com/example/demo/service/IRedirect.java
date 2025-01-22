package com.example.demo.service;

public interface IRedirect {

    String getRedirect(String hash);

    String getRedirectRedis(String hash);

}
