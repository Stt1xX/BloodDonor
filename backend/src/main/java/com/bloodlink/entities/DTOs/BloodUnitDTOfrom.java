package com.bloodlink.entities.DTOs;

import com.bloodlink.entities.BloodUnit;
import com.bloodlink.entities.enums.BloodGroup;
import com.bloodlink.entities.enums.RhFactor;
import com.bloodlink.validators.annotaions.ValidBloodUnitDates;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@ValidBloodUnitDates
public class BloodUnitDTOfrom {
    private Long id;
    @NotNull(message = "Группа крови не может быть пустой")
    private BloodGroup bloodGroup;
    @NotNull(message = "Rh фактор не может быть пустым")
    private RhFactor rhesusFactor;
    @NotNull(message = "Дата конца срока годности не может быть пустой")
    private LocalDate expirationDate;
    @NotNull(message = "Дата поставки не может быть пустой")
    private LocalDate createdAt;
    @NotNull(message = "Объем не может быть пустым")
    private Double volume;


    public BloodUnit convert() {
        return new BloodUnit(id, volume, bloodGroup, rhesusFactor, createdAt, expirationDate, null);
    }
}
