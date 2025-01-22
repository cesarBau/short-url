package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;

import com.example.demo.service.IRedirect;

import lombok.extern.log4j.Log4j2;

@Controller
@RequestMapping("/redirect")
@Log4j2
public class RedirectController {

    @Autowired
    private final IRedirect iRedirect;

    public RedirectController(IRedirect iRedirect) {
        this.iRedirect = iRedirect;
    }

    @RequestMapping("/{id}")
    public RedirectView redirect(@PathVariable String id) {
        log.info("Consume controller Redirect");
        log.info("Search to: " + id);
        String url = iRedirect.getRedirectRedis(id);
        log.info("Redirect to: " + url);
        return new RedirectView(url);
    }

}
