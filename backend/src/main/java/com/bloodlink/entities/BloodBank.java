package com.bloodlink.entities;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Data
@Entity
@Table(name = "blood_banks")
public class BloodBank {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String address;
    private String phone;
    private String workTime;

    @OneToMany(mappedBy = "bloodBank", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<BloodUnit> bloodUnits;

    @OneToMany(mappedBy = "bloodBank", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<BloodRequest> requests;
} 