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

import com.example.demo.entity.Domain;
import com.example.demo.entity.OriginUrl;
import com.example.demo.model.Url;
import com.example.demo.model.dto.UrlResponseDto;
import com.example.demo.repository.Domainrepository;
import com.example.demo.repository.OriginUrlRepository;
import com.google.common.hash.Hashing;

@Service
@PropertySource("classpath:other.properties")
public class OriginUrlService implements IOriginUrl {

    @Value("${url.expired}")
    private Long daysExpired;

    private static Logger logger = LoggerFactory.getLogger(OriginUrlService.class);
    private OriginUrlRepository originUrlRepository;
    private Domainrepository domainrepository;

    public OriginUrlService(OriginUrlRepository originUrlRepository, Domainrepository domainrepository) {
        this.originUrlRepository = originUrlRepository;
        this.domainrepository = domainrepository;
    }

    @Override
    public UrlResponseDto findByHash(String hash) {
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
        UrlResponseDto response = new UrlResponseDto();
        response.setUrl(originUrl.getUrlOrigin());
        response.setDomain(originUrl.getDomain().getName());
        response.setShortUrl(originUrl.getDomain().getValue() + originUrl.getHash());
        response.setExpiration(originUrl.getExpiration());
        return response;
    }

    @Override
    public UrlResponseDto createUrl(Url url) {
        logger.info("Consume service createUrl");
        logger.info("Creating url: " + url.getUrl());
        logger.info("Finding domain: " + url.getDomain());
        Domain domain = domainrepository.findByName(url.getDomain());
        if (domain == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "the domain not found", null);
        }
        LocalDateTime now = LocalDateTime.now();
        String hash = Hashing.murmur3_128().hashString(url.getUrl() + now.toString(), StandardCharsets.UTF_8)
                .toString();
        LocalDateTime expiration = LocalDateTime.now().plusDays(daysExpired);
        OriginUrl originUrl = new OriginUrl();
        originUrl.setUrlOrigin(url.getUrl());
        originUrl.setHash(hash);
        originUrl.setExpiration(expiration.toString());
        originUrl.setCreation(now);
        originUrl.setUpdate(now);
        originUrl.setDomain(domain);
        logger.info(originUrl.toString());
        OriginUrl processUrl = originUrlRepository.save(originUrl);
        UrlResponseDto response = new UrlResponseDto();
        response.setUrl(processUrl.getUrlOrigin());
        response.setDomain(processUrl.getDomain().getName());
        response.setShortUrl(processUrl.getDomain().getValue() + processUrl.getHash());
        response.setExpiration(processUrl.getExpiration());
        logger.info(response.toString());
        return response;
    }

    @Override
    public String getRedirect(String hash) {
        logger.info("Consume service getRedirect");
        logger.info("Finding by hash: " + hash);
        OriginUrl originUrl = originUrlRepository.findByHash(hash);
        if (originUrl == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "the hash does not exist", null);
        }
        LocalDateTime expiration = LocalDateTime.parse(originUrl.getExpiration());
        LocalDateTime now = LocalDateTime.now();
        if (expiration.isBefore(now)) {
            logger.error("The url has expired, proceeds to eliminate");
            originUrlRepository.deleteById(originUrl.getId());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "the url has expired", null);
        }
        return originUrl.getUrlOrigin();
    }

}
