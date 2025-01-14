package com.example.demo.service;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.demo.entity.OriginUrl;
import com.example.demo.model.Url;
import com.example.demo.repository.OriginUrlRepository;
import com.google.common.hash.Hashing;

@Service
@PropertySource("classpath:other.properties")
public class OriginUrlService implements IOriginUrl {

    @Value("${url.expired}")
    private Long daysExpired;

    private static Logger logger = LoggerFactory.getLogger(OriginUrlService.class);
    private OriginUrlRepository originUrlRepository;

    public OriginUrlService(OriginUrlRepository originUrlRepository) {
        this.originUrlRepository = originUrlRepository;
    }

    @Override
    public OriginUrl findByHash(String hash) {
        logger.info("Consume service findByHash");
        logger.info("Finding by hash: " + hash);
        OriginUrl originUrl = originUrlRepository.findByHash(hash);
        if (originUrl == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "the hash does not exist", null);
        }
        logger.info("Validate if the url is not expired");
        LocalDateTime expiration = LocalDateTime.parse(originUrl.getExpiration());
        LocalDateTime now = LocalDateTime.now();
        if (expiration.isBefore(now)) {
            logger.error("The url has expired, proceeds to eliminate");
            originUrlRepository.deleteById(originUrl.getId());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "the url has expired", null);
        }
        return originUrl;
    }

    @Override
    public OriginUrl createUrl(Url url) {
        logger.info("Consume service createUrl");
        logger.info("Creating url: " + url.getUrl());
        String hash = Hashing.murmur3_128().hashString(url.getUrl(), StandardCharsets.UTF_8).toString();
        LocalDateTime expiration = LocalDateTime.now().plusDays(daysExpired);
        LocalDateTime now = LocalDateTime.now();
        OriginUrl originUrl = new OriginUrl();
        originUrl.setUrlOrigin(url.getUrl());
        originUrl.setHash(hash);
        originUrl.setExpiration(expiration.toString());
        originUrl.setCreation(now);
        originUrl.setUpdate(now);
        logger.info(originUrl.toString());
        OriginUrl response = originUrlRepository.save(originUrl);
        logger.info(response.toString());
        return response;
    }

}
