package com.example.demo.service;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.demo.entity.OriginUrl;
import com.example.demo.entity.StatusUrl;
import com.example.demo.repository.OriginUrlRepository;

@Service
@PropertySource("classpath:other.properties")
public class RedirectService implements IRedirect {

    @Value("${url.expired}")
    private Long daysExpired;

    @Value("${url.error}")
    private String urlError;

    private static Logger logger = LoggerFactory.getLogger(RedirectService.class);
    private OriginUrlRepository originUrlRepository;

    public RedirectService(OriginUrlRepository originUrlRepository) {
        this.originUrlRepository = originUrlRepository;
    }

    @Override
    public String getRedirect(String hash) {
        logger.info("Consume service getRedirect");
        logger.info("Finding by hash: " + hash);
        OriginUrl originUrl = originUrlRepository.findByHash(hash);
        if (originUrl == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "the url not found", null);
        }
        if (originUrl.getStatusUrl().getStatus().equals("INACTIVE")) {
            logger.info("The url is inactive");
            return urlError;
        }
        String response = originUrl.getUrlOrigin();
        LocalDateTime expiration = LocalDateTime.parse(originUrl.getExpiration());
        LocalDateTime now = LocalDateTime.now();
        if (expiration.isBefore(now)) {
            logger.info("The url has expired");
            originUrl.setStatusUrl(new StatusUrl());
            originUrl.setUpdate(LocalDateTime.now());
            originUrlRepository.save(originUrl);
            response = urlError;
        }
        return response;
    }

}
