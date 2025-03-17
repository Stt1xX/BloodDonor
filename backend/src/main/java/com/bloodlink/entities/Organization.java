package com.bloodlink.entities;

import jakarta.persistence.*;
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
    private String workTime;
}
