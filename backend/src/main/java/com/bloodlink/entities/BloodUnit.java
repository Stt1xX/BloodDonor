package com.bloodlink.entities;

import com.bloodlink.entities.enums.BloodGroup;
import com.bloodlink.entities.enums.RhFactor;
import com.bloodlink.entities.enums.converters.BloodTypeConverter;
import com.bloodlink.entities.enums.converters.RhFactorConverter;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;

@Data
@Entity
@Table(name = "blood_units")
@AllArgsConstructor
public class BloodUnit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Double volume;

    @Convert(converter = BloodTypeConverter.class)
    private BloodGroup bloodGroup;

    @Convert(converter = RhFactorConverter.class)
    private RhFactor rhFactor;

    private LocalDate createdAt;
    private LocalDate expirationDate;


    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "blood_bank_id")
    private Organization bloodBank;

    public BloodUnit() {

    }
}