package com.bloodlink.entities;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Data
@Entity
@Table(name = "medical_institutions")
public class MedicalInstitution {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String address;
    private String phone;
    private String workTime;

    @OneToMany(mappedBy = "medicalInstitution", cascade = CascadeType.ALL)
    private List<BloodRequest> requests;
} 