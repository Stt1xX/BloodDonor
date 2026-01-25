package com.bloodlink.dto.response;

import com.bloodlink.entities.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Response DTO for registration request.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegistrationRequestResponse {

    private Long id;
    private String name;
    private String surname;
    private String email;
    private Role role;
    private LocalDateTime createdAt;
    private String organizationName;
}
