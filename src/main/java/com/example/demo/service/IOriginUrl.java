package com.example.demo.service;

import com.example.demo.entity.OriginUrl;
import com.example.demo.model.Url;

public interface IOriginUrl {

    OriginUrl findByHash(String hash);

    OriginUrl createUrl(Url url);

}
