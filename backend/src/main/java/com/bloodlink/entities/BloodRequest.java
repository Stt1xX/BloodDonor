package com.bloodlink.entities;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "blood_requests")
public class BloodRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private BloodType bloodType;

    @Enumerated(EnumType.STRING)
    private RhFactor rhFactor;

    private Double volumeNeeded;

    @Enumerated(EnumType.STRING)
    private RequestStatus status;

    private Boolean urgent;

    @ManyToOne
    @JoinColumn(name = "blood_bank_id")
    private BloodBank bloodBank;

    @ManyToOne
    @JoinColumn(name = "medical_institution_id")
    private MedicalInstitution medicalInstitution;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String rejectionReason;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        status = RequestStatus.PENDING;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
} 