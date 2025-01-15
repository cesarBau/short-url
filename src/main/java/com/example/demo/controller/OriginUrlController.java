package com.example.demo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.Url;
import com.example.demo.model.dto.UrlResponseDto;
import com.example.demo.service.IOriginUrl;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/short-url")
public class OriginUrlController {

    private static Logger logger = LoggerFactory.getLogger(OriginUrlController.class);

    private IOriginUrl originUrlService;

    public OriginUrlController(IOriginUrl originUrlService) {
        this.originUrlService = originUrlService;
    }

    @GetMapping("/{hash}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public UrlResponseDto getShortUrl(@PathVariable String hash) {
        logger.info("Consume controller getShortUrl");
        UrlResponseDto originUrl = originUrlService.findByHash(hash);
        return originUrl;
    }

    @PostMapping("/")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public UrlResponseDto createShortUrl(@RequestBody Url entity) {
        logger.info("Consume controller createShortUrl");
        UrlResponseDto originUrl = originUrlService.createUrl(entity);
        return originUrl;
    }

}
