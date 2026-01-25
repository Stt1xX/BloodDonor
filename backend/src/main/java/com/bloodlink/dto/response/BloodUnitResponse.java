package com.bloodlink.dto.response;

import com.bloodlink.entities.enums.BloodGroup;
import com.bloodlink.entities.enums.RhFactor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Response DTO for blood unit.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BloodUnitResponse {
    
    private Long id;
    private BloodGroup bloodGroup;
    private RhFactor rhesusFactor;
    private Double volume;
    private LocalDate expirationDate;
    private LocalDate createdAt;
}
