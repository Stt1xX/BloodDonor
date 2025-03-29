package com.bloodlink.entities.DTOs;

import com.bloodlink.entities.BloodUnit;
import com.bloodlink.entities.enums.BloodType;
import com.bloodlink.entities.enums.RhFactor;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@AllArgsConstructor
@Data
public class BloodUnitDTOto {
    private Long id;
    private BloodType bloodGroup;
    private RhFactor rhesusFactor;
    private Double volume;

    private LocalDateTime expiryDate;
    private LocalDateTime createdAt;

    public static BloodUnitDTOto convert(BloodUnit entity) {
        return new BloodUnitDTOto(entity.getId(), entity.getBloodType(), entity.getRhFactor(), entity.getVolume(),
                entity.getExpirationDate(), entity.getCreatedAt());
    }


}
