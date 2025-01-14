package com.example.demo.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
@Entity
@Table(name = "url")
public class OriginUrl {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, name = "url_origin")
    private String urlOrigin;
    @Column(nullable = false)
    private String hash;
    @Column(nullable = false)
    private String expiration;
    @CreationTimestamp
    @Column(name = "creation_date")
    private LocalDateTime creation;
    @CreationTimestamp
    @Column(name = "last_update")
    private LocalDateTime update;
    @ManyToOne(targetEntity = Domain.class)
    @JoinColumn(name = "domain", referencedColumnName = "id")
    private Domain domain;

}
