package com.bloodlink.entities;

import com.bloodlink.entities.id.BloodReserveId;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@Entity
@Table(name = "blood_reserves")
@AllArgsConstructor
public class BloodReserve {
    @EmbeddedId
    private BloodReserveId id;

    @ManyToOne
    @MapsId("bankId")
    @JoinColumn(name = "bank_id", nullable = false)
    private Organization bank;

    private Double totalQuantity;

    public BloodReserve() {}
}
