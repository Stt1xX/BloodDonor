package com.bloodlink.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@MappedSuperclass
public abstract class Organization {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;
    private String address;
    private String phone;
    private Integer hoursFrom;
    private Integer hoursTo;
    private Integer minutesFrom;
    private Integer minutesTo;
}
