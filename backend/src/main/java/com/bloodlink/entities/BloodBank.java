package com.bloodlink.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "blood_banks")
public class BloodBank extends Organization{

    @OneToMany(mappedBy = "bloodBank", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<BloodUnit> bloodUnits;

    @OneToMany(mappedBy = "bloodBank", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<BloodRequest> requests;

    public BloodBank() {
    }
} 