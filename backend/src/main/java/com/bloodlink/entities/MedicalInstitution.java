package com.bloodlink.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "medical_institutions")
public class MedicalInstitution extends Organization{

    @OneToMany(mappedBy = "medicalInstitution", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<BloodRequest> requests;

    public MedicalInstitution() {
    }
} 