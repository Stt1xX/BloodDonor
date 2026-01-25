package com.bloodlink.dto.response;

import com.bloodlink.entities.enums.OrganizationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Response DTO for organization.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrganizationResponse {

    private Long id;
    private String name;
    private String address;
    private String phone;
    private Integer hoursFrom;
    private Integer hoursTo;
    private Integer minutesFrom;
    private Integer minutesTo;
    private OrganizationType organizationType;
}
