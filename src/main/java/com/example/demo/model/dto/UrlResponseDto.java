package com.example.demo.model.dto;

import com.example.demo.model.Url;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@Setter
@Getter
@ToString
public class UrlResponseDto extends Url {

    @JsonProperty("short_url")
    private String shortUrl;
    private String expiration;

    public UrlResponseDto(Url url, String shortUrl, String expiration) {
        super(url.getUrl(), url.getDomain());
        this.shortUrl = shortUrl;
        this.expiration = expiration;
    }

}
