package com.example.demo.service;

import com.example.demo.model.Url;
import com.example.demo.model.dto.UrlResponseDto;

public interface IOriginUrl {

    UrlResponseDto findByHash(String hash);

    UrlResponseDto createUrl(Url url);

}
