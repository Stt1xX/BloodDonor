package com.bloodlink.dto.response;

import com.bloodlink.entities.enums.BloodGroup;
import com.bloodlink.entities.enums.RequestStatus;
import com.bloodlink.entities.enums.RhFactor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Response DTO for blood request viewed by blood bank.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BloodRequestBankResponse {

    private Long id;
    private Long requestId;
    private BloodGroup bloodGroup;
    private RhFactor rhesusFactor;
    private String description;
    private Double volumeNeeded;
    private Boolean isEmergency;
    private RequestStatus status;
    private String rejectionReason;
    private OrganizationResponse institution;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private UserResponse banker;
    private UserResponse creator;
}
