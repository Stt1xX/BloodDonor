package com.bloodlink.entities;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "medical_institutions")
public class MedicalInstitution extends Organization{

    @OneToMany(mappedBy = "medicalInstitution", cascade = CascadeType.ALL)
    private List<BloodRequest> requests;

    public MedicalInstitution() {
    }
} 