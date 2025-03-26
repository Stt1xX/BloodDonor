package com.bloodlink.entities;

import com.bloodlink.entities.enums.BloodType;
import com.bloodlink.entities.enums.RhFactor;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "blood_units")
public class BloodUnit {

    public BloodUnit() {

    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private BloodType bloodType;

    @Enumerated(EnumType.STRING)
    private RhFactor rhFactor;

    private Double volume;

    private LocalDateTime expirationDate;

    @ManyToOne
    @JoinColumn(name = "blood_bank_id")
    private Organization bloodBank;

    @CreatedDate
    private LocalDateTime createdAt;

    public BloodUnit(Long id, BloodType bloodType, RhFactor rhFactor, Double volume, LocalDateTime expirationDate) {
        this.id = id;
        this.bloodType = bloodType;
        this.rhFactor = rhFactor;
        this.volume = volume;
        this.expirationDate = expirationDate;
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
} 