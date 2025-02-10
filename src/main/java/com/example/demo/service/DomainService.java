package com.example.demo.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.demo.entity.Domain;
import com.example.demo.repository.Domainrepository;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class DomainService implements IDomain {

    private Domainrepository domainRepository;

    public DomainService(Domainrepository domainRepository) {
        this.domainRepository = domainRepository;
    }

    @Override
    public Domain getDomainByName(String name) {
        log.info("Consume service getDomainByName");
        Domain response = domainRepository.findByName(name);
        if (response == null) {
            log.info("No domains found");
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "domain not found", null);
        }
        return response;
    }

    @Override
    public List<Domain> getDomains() {
        log.info("Consume service getDomains");
        List<Domain> response = domainRepository.findAll();
        return response;
    }

}
