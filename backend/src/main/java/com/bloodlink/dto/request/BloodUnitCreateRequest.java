package com.bloodlink.dto.request;

import com.bloodlink.entities.enums.BloodGroup;
import com.bloodlink.entities.enums.RhFactor;
import com.bloodlink.validators.annotaions.ValidBloodUnitDates;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Request DTO for creating or updating a blood unit.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ValidBloodUnitDates
public class BloodUnitCreateRequest {
    
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
}
