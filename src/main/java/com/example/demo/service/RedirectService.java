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

    public RedirectService(OriginUrlRepository originUrlRepository) {
        this.originUrlRepository = originUrlRepository;
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

}
