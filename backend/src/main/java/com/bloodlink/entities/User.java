package com.bloodlink.entities;

import jakarta.persistence.*;
import lombok.Data;
import com.bloodlink.entities.enums.Role;

@Data
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String surname;
    private String email;
    private String password;
    private String post;
    private Boolean isDeleted;

    @Enumerated(EnumType.STRING)
    private Role role;

    @ManyToOne
    @JoinColumn(name = "organization_id")
    private Organization organization;
} 