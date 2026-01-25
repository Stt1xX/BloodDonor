package com.bloodlink.dto.request;

import com.bloodlink.entities.enums.BloodGroup;
import com.bloodlink.entities.enums.RhFactor;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Request DTO for creating a new blood request.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BloodRequestCreateRequest {

    @NotNull(message = "Группа крови не может быть пустой")
    private BloodGroup bloodGroup;
    
    @NotNull(message = "Rh фактор не может быть пустым")
    private RhFactor rhesusFactor;
    
    @NotNull(message = "Запрашиваемый объем не может быть пустым")
    private Double volumeNeeded;
    
    @NotNull(message = "Обозначение срочности не должно быть пустым")
    private Boolean isEmergency;
    
    @NotNull(message = "Лист id банков должен быть предоставлен")
    @NotEmpty(message = "Лист id банков не может быть пустым")
    private List<Long> bloodBanks;

    @NotNull(message = "Описание запроса не должно быть пустым")
    @NotEmpty(message = "Описание не может быть пустым")
    private String description;
}
