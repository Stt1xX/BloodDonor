package com.bloodlink.entities.DTOs;

import com.bloodlink.entities.BloodUnit;
import com.bloodlink.entities.enums.BloodType;
import com.bloodlink.entities.enums.RhFactor;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@AllArgsConstructor
@Data
public class BloodUnitDTOfrom {
    private BloodType bloodGroup;
    private RhFactor rhesusFactor;
    private Double volume;
    private LocalDateTime expiryDate;


    public static BloodUnit convert(BloodUnitDTOfrom dto) {
        return new BloodUnit(null, dto.getBloodGroup(), dto.getRhesusFactor(), dto.getVolume(),
                dto.getExpiryDate());
    }


}
