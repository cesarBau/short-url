package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.demo.model.Visit;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class VisitService implements IVisit {

    @Autowired
    private IAmqp iAmqp;

    @Autowired
    private ObjectMapper objectMapper;

    public VisitService(IAmqp iAmqp) {
        this.iAmqp = iAmqp;
    }

    @Override
    public void saveVisit(Visit visit) {
        log.info("Consume service saveVisit");
        try {
            String send = objectMapper.writeValueAsString(visit);
            iAmqp.generateMessage(send);
        } catch (JsonProcessingException e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "Error to create visit", e);
        }
    }
}
