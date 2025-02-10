package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.Domain;
import com.example.demo.service.IDomain;

import lombok.extern.log4j.Log4j2;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Log4j2
@RestController
@RequestMapping("/domain")
public class DomainController {

    @Autowired
    private final IDomain iDomain;

    public DomainController(IDomain iDomain) {
        this.iDomain = iDomain;
    }

    @GetMapping("/")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<Domain> getAllDomains() {
        log.info("Consume controller getAllDomains");
        return iDomain.getDomains();
    }

    @GetMapping("/{name}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Domain getDomainByName(@PathVariable String name) {
        log.info("Consume controller getDomainByName");
        return iDomain.getDomainByName(name);
    }

}
