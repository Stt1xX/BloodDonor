package com.bloodlink.entities.id;

import com.bloodlink.entities.enums.BloodGroup;
import com.bloodlink.entities.enums.RhFactor;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.Objects;

@Data
@AllArgsConstructor
@Embeddable
public class BloodReserveId implements Serializable {
    private BloodGroup bloodGroup;
    private RhFactor rhFactor;
    private Long bankId;

    public BloodReserveId() {}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BloodReserveId that = (BloodReserveId) o;
        return Objects.equals(bloodGroup, that.bloodGroup) &&
               Objects.equals(rhFactor, that.rhFactor) &&
               Objects.equals(bankId, that.bankId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bloodGroup, rhFactor, bankId);
    }
}
