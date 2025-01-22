package com.example.demo.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.demo.entity.OriginUrl;
import com.example.demo.entity.StatusUrl;
import com.example.demo.entity.Visit;
import com.example.demo.repository.OriginUrlRepository;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
@PropertySource("classpath:other.properties")
public class RedirectService implements IRedirect {

    @Value("${url.error}")
    private String urlError;

    private OriginUrlRepository originUrlRepository;

    @Autowired
    private IVisit iVisit;

    @Autowired
    private ICrudRedis iCrudRedis;

    @Autowired
    private IOriginUrl iOriginUrl;

    public RedirectService(OriginUrlRepository originUrlRepository, ICrudRedis iCrudRedis, IOriginUrl iOriginUrl) {
        this.originUrlRepository = originUrlRepository;
        this.iCrudRedis = iCrudRedis;
        this.iOriginUrl = iOriginUrl;
    }

    @Override
    public String getRedirect(String hash) {
        log.info("Consume service getRedirect");
        log.info("Finding by hash: " + hash);
        OriginUrl originUrl = originUrlRepository.findByHash(hash);
        Visit visit = new Visit();
        if (originUrl == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "the url not found", null);
        }
        visit.setHash(hash);
        visit.setStatusUrl(originUrl.getStatusUrl());
        visit.setConsume(LocalDateTime.now());
        if (originUrl.getStatusUrl().getStatus().equals("INACTIVE")) {
            log.info("The url is inactive");
            iVisit.saveVisit(visit);
            return urlError;
        }
        String response = originUrl.getUrlOrigin();
        LocalDateTime expiration = LocalDateTime.parse(originUrl.getExpiration());
        LocalDateTime now = LocalDateTime.now();
        if (expiration.isBefore(now)) {
            log.info("The url has expired");
            originUrl.setStatusUrl(new StatusUrl());
            originUrl.setUpdate(LocalDateTime.now());
            originUrlRepository.save(originUrl);
            visit.setStatusUrl(originUrl.getStatusUrl());
            response = urlError;
        }
        iVisit.saveVisit(visit);
        return response;
    }

    @Override
    public String getRedirectRedis(String hash) {
        log.info("Consume service getRedirectRedis");
        // Search to redis origin url
        String searchRedis = iCrudRedis.find(hash);
        if (searchRedis == null) {
            log.info("The url not found in redis, search in DB");
            return getRedirect(hash);
        }
        Visit visit = new Visit();
        visit.setHash(hash);
        visit.setConsume(LocalDateTime.now());
        visit.setStatusUrl(new StatusUrl());
        String[] valuesSplit = searchRedis.split("\\|");
        LocalDateTime expiration = LocalDateTime.parse(valuesSplit[1]);
        if (expiration.isBefore(LocalDateTime.now())) {
            log.info("The url has expired");
            iOriginUrl.changeStatus(hash);
            iVisit.saveVisit(visit);
            iCrudRedis.delete(hash);
            return urlError;
        }
        visit.setStatusUrl(new StatusUrl((long) 1, "ACTIVE", "The url is active"));
        iVisit.saveVisit(visit);
        return valuesSplit[0];
    }

}
