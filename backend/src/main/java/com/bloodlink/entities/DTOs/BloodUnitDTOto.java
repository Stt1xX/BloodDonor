package com.bloodlink.entities.DTOs;

import com.bloodlink.entities.BloodUnit;
import com.bloodlink.entities.enums.BloodGroup;
import com.bloodlink.entities.enums.RhFactor;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@AllArgsConstructor
@Data
public class BloodUnitDTOto {
    private Long id;
    private BloodGroup bloodGroup;
    private RhFactor rhesusFactor;
    private Double volume;

    private LocalDate expirationDate;
    private LocalDate createdAt;

    public static BloodUnitDTOto convert(BloodUnit entity) {
        return new BloodUnitDTOto(entity.getId(), entity.getBloodGroup(), entity.getRhFactor(), entity.getVolume(),
                entity.getExpirationDate(), entity.getCreatedAt());
    }


}
