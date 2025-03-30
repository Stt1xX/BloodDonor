package com.bloodlink.entities.DTOs;

import com.bloodlink.entities.BloodRequest;
import com.bloodlink.entities.Organization;
import com.bloodlink.entities.enums.BloodGroup;
import com.bloodlink.entities.enums.RequestStatus;
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


    public BloodRequest convert(Organization institution) {
        var req = new BloodRequest();
        req.setBloodGroup(bloodGroup);
        req.setRhFactor(rhesusFactor);
        req.setVolumeNeeded(volumeNeeded);
        req.setIsEmergency(isEmergency);
        req.setMedicalInstitution(institution);
        req.setStatus(RequestStatus.PENDING);
        return req;
    }
}
