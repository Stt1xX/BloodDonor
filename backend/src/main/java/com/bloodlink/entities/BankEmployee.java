package com.bloodlink.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@Table(name = "bank_employees")
@EqualsAndHashCode(callSuper = true)
public class BankEmployee extends User {
    @ManyToOne
    @JoinColumn(name = "bank_id")
    private BloodBank bloodBank;

    private String post;
} 