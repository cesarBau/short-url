package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.Visit;

public interface VisitRepository extends JpaRepository<Visit, Long> {

}
