package com.bloodlink.entities;

import jakarta.persistence.*;
import lombok.Data;

import com.bloodlink.entities.enums.Role;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Data
@Entity
public class RegistrationRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String surname;

    @Column(unique = true)
    private String email;

    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToOne
    @JoinColumn(name = "organization_d")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Organization organization;

    @CreatedDate
    private LocalDateTime createdAt;
}
