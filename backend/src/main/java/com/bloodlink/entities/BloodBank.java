package com.bloodlink.entities;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "blood_banks")
public class BloodBank extends Organization{

    @OneToMany(mappedBy = "bloodBank", cascade = CascadeType.ALL)
    private List<BloodUnit> bloodUnits;

    @OneToMany(mappedBy = "bloodBank", cascade = CascadeType.ALL)
    private List<BloodRequest> requests;

    public BloodBank() {
    }
} 