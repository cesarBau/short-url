package com.example.demo.service;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.demo.entity.Domain;
import com.example.demo.entity.OriginUrl;
import com.example.demo.entity.StatusUrl;
import com.example.demo.model.Url;
import com.example.demo.model.dto.UrlResponseDto;
import com.example.demo.repository.Domainrepository;
import com.example.demo.repository.OriginUrlRepository;
import com.google.common.hash.Hashing;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
@PropertySource("classpath:other.properties")
public class OriginUrlService implements IOriginUrl {

    @Value("${url.expired}")
    private Long daysExpired;

    @Value("${url.error}")
    private String urlError;

    private OriginUrlRepository originUrlRepository;
    private Domainrepository domainrepository;

    @Autowired
    private ICrudRedis iCrudRedis;

    public OriginUrlService(OriginUrlRepository originUrlRepository, Domainrepository domainrepository,
            ICrudRedis iCrudRedis) {
        this.originUrlRepository = originUrlRepository;
        this.domainrepository = domainrepository;
        this.iCrudRedis = iCrudRedis;
    }

    @Override
    public UrlResponseDto findByHash(String hash) {
        log.info("Consume service findByHash");
        log.info("Finding by hash: " + hash);
        OriginUrl originUrl = originUrlRepository.findByHash(hash);
        if (originUrl == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "the hash does not exist", null);
        }
        log.info("Validate if the url is not expired");
        UrlResponseDto response = createResponse(originUrl);
        return response;
    }

    @Override
    public UrlResponseDto createUrl(Url url) {
        log.info("Consume service createUrl");
        log.info("Creating url: " + url.getUrl());
        log.info("Finding domain: " + url.getDomain());
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
        originUrl.setStatusUrl(new StatusUrl((long) 1, "ACTIVE", "The url is active"));
        log.info(originUrl.toString());
        OriginUrl processUrl = originUrlRepository.save(originUrl);
        UrlResponseDto response = createResponse(processUrl);
        log.info(response.toString());
        log.info("Insert to redis origin url");
        long expireAt = 60 * 60 * 24 * (daysExpired + 1);
        iCrudRedis.save(processUrl.getHash(), processUrl.getUrlOrigin() + "|" + processUrl.getExpiration(), expireAt);
        return response;
    }

    @SuppressWarnings("unused")
    private UrlResponseDto createResponse(OriginUrl processUrl) {
        log.info("Consume service createResponse");
        UrlResponseDto response = new UrlResponseDto();
        response.setUrl(processUrl.getUrlOrigin());
        response.setDomain(processUrl.getDomain().getName());
        response.setShortUrl(processUrl.getDomain().getValue() + processUrl.getHash());
        response.setExpiration(processUrl.getExpiration());
        response.setIsActive(processUrl.getStatusUrl().getStatus());
        return response;
    }

    @Override
    public void changeStatus(String hash) {
        log.info("Consume service changeStatus");
        log.info("Finding by hash: " + hash);
        OriginUrl originUrl = originUrlRepository.findByHash(hash);
        if (originUrl == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "the hash does not exist", null);
        }
        originUrl.setStatusUrl(new StatusUrl());
        originUrl.setUpdate(LocalDateTime.now());
        originUrlRepository.save(originUrl);
    }

}
