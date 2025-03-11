package com.example.todo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BloodDonor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String fullName;

    @NotBlank
    private String bloodType;

    private String phoneNumber;

    private boolean isAvailable;

    private String lastDonationDate;

    public BloodDonor(String fullName, String bloodType) {
        this.fullName = fullName;
        this.bloodType = bloodType;
        this.isAvailable = true;
    }
} 