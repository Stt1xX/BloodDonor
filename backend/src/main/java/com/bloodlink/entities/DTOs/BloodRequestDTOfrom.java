package com.bloodlink.entities.DTOs;

import com.bloodlink.entities.BloodRequest;
import com.bloodlink.entities.User;
import com.bloodlink.entities.enums.BloodGroup;
import com.bloodlink.entities.enums.RhFactor;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class BloodRequestDTOfrom {

    @NotNull(message = "Группа крови не может быть пустой")
    private BloodGroup bloodGroup;
    @NotNull(message = "Rh фактор не может быть пустым")
    private RhFactor rhesusFactor;
    @NotNull(message = "Запрашиваемый объем не может быть пустым")
    private Double volumeNeeded;
    @NotNull(message = "Обозначение срочности не должно быть пустым")
    private Boolean isEmergency;
    @NotNull(message = "Лист id банков должен быть предоставлен")
    @NotEmpty
    private List<Long> bloodBanks;

    @NotNull(message = "Описание запроса не должно быть пустым")
    @NotEmpty
    private String description;


    public BloodRequest convert(User caller) {
        var req = new BloodRequest();
        req.setBloodGroup(bloodGroup);
        req.setRhFactor(rhesusFactor);
        req.setVolumeNeeded(volumeNeeded);
        req.setIsEmergency(isEmergency);
        req.setMedicalInstitution(caller.getOrganization());
        req.setCreator(caller);
        req.setDescription(description);
        return req;
    }
}
