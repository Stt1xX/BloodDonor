package com.bloodlink.entities;

import com.bloodlink.entities.enums.Role;
import jakarta.persistence.*;
import lombok.Data;

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

    public boolean isBankEmployee() {
        return role.equals(Role.BANK_EMPLOYEE);
    }
    public boolean isMedicalEmployee() {
        return role.equals(Role.MEDICAL_EMPLOYEE);
    }


} 