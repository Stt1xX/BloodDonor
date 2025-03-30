package com.bloodlink.entities.DTOs;

import com.bloodlink.entities.BloodRequest;
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
    private BloodGroup bloodGroup;
    private Long requestGroupId;
    private RhFactor rhesusFactor;
    private Double volumeNeeded;
    private RequestStatus status;
    private OrganizationDTOto bank;
    private Boolean isEmergency;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String rejectionReason;

    public static BloodRequestDTOtoMed convert(BloodRequest bloodRequest) {
        return new BloodRequestDTOtoMed(bloodRequest.getId(), bloodRequest.getBloodGroup(),
                bloodRequest.getRequestGroupId(),
                bloodRequest.getRhFactor(), bloodRequest.getVolumeNeeded(), bloodRequest.getStatus(),
                OrganizationDTOto.convert(bloodRequest.getBloodBank()), bloodRequest.getIsEmergency(),
                bloodRequest.getCreatedAt(), bloodRequest.getUpdatedAt(), bloodRequest.getRejectionReason());
    }
}
