package com.bloodlink.entities.DTOs;

import com.bloodlink.entities.BloodUnit;
import com.bloodlink.entities.enums.BloodType;
import com.bloodlink.entities.enums.RhFactor;
import lombok.Data;

import java.time.LocalDateTime;


@Data
public class BloodUnitDto {
    private Long id;
    private BloodType bloodType;
    private RhFactor rhFactor;
    private Double volume;

    private LocalDateTime expirationDate;
    private LocalDateTime createdAt;

    public BloodUnitDto(Long id, BloodType bloodType, RhFactor rhFactor, Double volume, LocalDateTime expirationDate, LocalDateTime createdAt) {
        this.id = id;
        this.bloodType = bloodType;
        this.rhFactor = rhFactor;
        this.volume = volume;
        this.expirationDate = expirationDate;
        this.createdAt = createdAt;
    }

    public static BloodUnitDto convert(BloodUnit entity) {
        return new BloodUnitDto(entity.getId(), entity.getBloodType(), entity.getRhFactor(), entity.getVolume(),
                entity.getExpirationDate(), entity.getCreatedAt());
    }

    public static BloodUnit convertFrom(BloodUnitDto dto) {
        return new BloodUnit(dto.getId(), dto.getBloodType(), dto.getRhFactor(), dto.getVolume(),
                dto.getExpirationDate());
    }


}
