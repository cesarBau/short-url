package com.example.demo.model;

import java.time.LocalDateTime;

import com.example.demo.entity.StatusUrl;

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
public class Visit {

    private Long id;

    private String hash;

    private LocalDateTime consume;

    private StatusUrl statusUrl;

}
