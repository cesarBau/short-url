package com.example.demo.service;

import org.springframework.stereotype.Service;

import com.example.demo.entity.Visit;
import com.example.demo.repository.VisitRepository;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class VisitService implements IVisit {

    VisitRepository visitRepository;

    public VisitService(VisitRepository visitRepository) {
        this.visitRepository = visitRepository;
    }

    @Override
    public void saveVisit(Visit visit) {
        log.info("Consume service saveVisit");
        visitRepository.save(visit);
    }
}
