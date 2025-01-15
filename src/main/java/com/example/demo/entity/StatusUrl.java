package com.example.demo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@Setter
@Getter
@ToString
@Entity
@Table(name = "status_url")
public class StatusUrl {

    @Id
    private Long id;
    @Column(nullable = false)
    private String status;
    @Column(nullable = false)
    private String description;

    public StatusUrl() {
        this.id = (long) 2;
        this.status = "INACTIVE";
        this.description = "The url has expired";
    }

}
