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
public class BloodRequestDTOtoMed {

    private Long id;
    private Long requestId;
    private BloodGroup bloodGroup;
    private RhFactor rhesusFactor;
    private String description;
    private Double volumeNeeded;
    private Boolean isEmergency;

    private RequestStatus status;
    private String rejectionReason;
    private OrganizationDTOto bank;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private UserDTOto banker;
    private UserDTOto creator;

    public static BloodRequestDTOtoMed convert(RequestToBank request) {
        var bloodRequest = request.getRequest();
        return new BloodRequestDTOtoMed(request.getId(), bloodRequest.getId(), bloodRequest.getBloodGroup(),
                bloodRequest.getRhFactor(), bloodRequest.getDescription(), bloodRequest.getVolumeNeeded(),
                bloodRequest.getIsEmergency(), request.getStatus(), request.getRejectionReason(),
                OrganizationDTOto.convert(request.getBloodBank()), request.getCreatedAt(),
                request.getUpdatedAt(), request.getBanker() != null ? UserDTOto.convert(request.getBanker()) : null,
                bloodRequest.getCreator() != null ? UserDTOto.convert(bloodRequest.getCreator()) : null);
    }
}
