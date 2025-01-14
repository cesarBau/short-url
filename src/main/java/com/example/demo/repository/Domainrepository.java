package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.Domain;

public interface Domainrepository extends JpaRepository<Domain, Long> {

    Domain findByName(String name);

}
