package com.example.demo.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.Url;
import com.example.demo.model.dto.UrlResponseDto;
import com.example.demo.service.IOriginUrl;

import lombok.extern.log4j.Log4j2;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Log4j2
@RestController
@RequestMapping("/short-url")
public class OriginUrlController {

    private IOriginUrl originUrlService;

    public OriginUrlController(IOriginUrl originUrlService) {
        this.originUrlService = originUrlService;
    }

    @GetMapping("/{hash}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public UrlResponseDto getShortUrl(@PathVariable String hash) {
        log.info("Consume controller getShortUrl");
        UrlResponseDto originUrl = originUrlService.findByHash(hash);
        return originUrl;
    }

    @PostMapping("/")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public UrlResponseDto createShortUrl(@RequestBody Url entity) {
        log.info("Consume controller createShortUrl");
        UrlResponseDto originUrl = originUrlService.createUrl(entity);
        return originUrl;
    }

}
