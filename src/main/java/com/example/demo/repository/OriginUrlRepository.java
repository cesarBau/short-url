package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.OriginUrl;

public interface OriginUrlRepository extends JpaRepository<OriginUrl, Long> {

    OriginUrl findByHash(String hash);

}
