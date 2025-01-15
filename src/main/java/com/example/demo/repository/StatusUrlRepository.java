package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.StatusUrl;

public interface StatusUrlRepository extends JpaRepository<StatusUrl, Long> {

    StatusUrl findByStatus(String status);

}
