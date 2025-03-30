package com.bloodlink.entities.DTOs;

import com.bloodlink.entities.RequestToBank;
import com.bloodlink.entities.enums.BloodGroup;
import com.bloodlink.entities.enums.RequestStatus;
import com.bloodlink.entities.enums.RhFactor;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class BloodRequestDTOtoBank {

    private Long id;
    private BloodGroup bloodGroup;
    private RhFactor rhesusFactor;
    private String description;
    private Double volumeNeeded;
    private Boolean isEmergency;

    private RequestStatus status;
    private String rejectionReason;
    private OrganizationDTOto institution;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private UserDTOto banker;
    private UserDTOto creator;

    public static BloodRequestDTOtoBank convert(RequestToBank request) {
        var bloodRequest = request.getRequest();
        return new BloodRequestDTOtoBank(bloodRequest.getId(), bloodRequest.getBloodGroup(),
                bloodRequest.getRhFactor(), bloodRequest.getDescription(), bloodRequest.getVolumeNeeded(),
                bloodRequest.getIsEmergency(), request.getStatus(), request.getRejectionReason(),
                OrganizationDTOto.convert(bloodRequest.getMedicalInstitution()), request.getCreatedAt(),
                request.getUpdatedAt(), UserDTOto.convert(request.getBanker()),
                UserDTOto.convert(bloodRequest.getCreator()));
    }
}
