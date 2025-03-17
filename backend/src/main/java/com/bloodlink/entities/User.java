package com.bloodlink.entities;

import jakarta.persistence.*;
import lombok.Data;
import com.bloodlink.entities.enums.Role;

@Data
@Entity
@Table(name = "users")
@Inheritance(strategy = InheritanceType.JOINED)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String surname;
    private String email;
    private String password;
    private Role role;
} 