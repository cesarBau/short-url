package com.example.demo.service;

import java.util.List;

import com.example.demo.entity.Domain;

public interface IDomain {

    Domain getDomainByName(String name);

    List<Domain> getDomains();

}
