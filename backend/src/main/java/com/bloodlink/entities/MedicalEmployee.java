package com.bloodlink.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@Table(name = "medical_employees")
@EqualsAndHashCode(callSuper = true)
public class MedicalEmployee extends User {
    @ManyToOne
    @JoinColumn(name = "institution_id")
    private MedicalInstitution institution;

    private String post;
} 