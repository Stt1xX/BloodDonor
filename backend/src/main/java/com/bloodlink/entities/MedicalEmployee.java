package com.bloodlink.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Data
@Entity
@Table(name = "medical_employees")
@EqualsAndHashCode(callSuper = true)
public class MedicalEmployee extends User {
    @ManyToOne
    @JoinColumn(name = "institution_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private MedicalInstitution institution;
} 