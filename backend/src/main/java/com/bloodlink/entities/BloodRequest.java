package com.bloodlink.entities;

import com.bloodlink.entities.enums.BloodType;
import com.bloodlink.entities.enums.RequestStatus;
import com.bloodlink.entities.enums.RhFactor;
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
    private Organization bloodBank;

    @ManyToOne
    @JoinColumn(name = "medical_institution_id")
    private Organization medicalInstitution;

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