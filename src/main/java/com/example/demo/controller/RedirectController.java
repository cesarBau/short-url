package com.example.demo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;

import com.example.demo.service.IRedirect;

@Controller
@RequestMapping("/redirect")
public class RedirectController {

    private static Logger logger = LoggerFactory.getLogger(RedirectController.class);
    private final IRedirect iRedirect;

    public RedirectController(IRedirect iRedirect) {
        this.iRedirect = iRedirect;
    }

    @RequestMapping("/{id}")
    public RedirectView redirect(@PathVariable String id) {
        logger.info("Consume controller Redirect");
        logger.info("Search to: " + id);
        String url = iRedirect.getRedirect(id);
        logger.info("Redirect to: " + url);
        return new RedirectView(url);
    }

}
