package com.bloodlink.entities;

import jakarta.persistence.*;
import lombok.Data;

import com.bloodlink.entities.enums.Role;
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
    @JoinColumn(name = "institution_id")
    private MedicalInstitution institution;

    @OneToOne
    @JoinColumn(name = "blood_bank_id")
    private BloodBank bloodBank;

    @CreatedDate
    private LocalDateTime createdAt;
}
