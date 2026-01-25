package com.bloodlink.dto.response;

import com.bloodlink.entities.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Response DTO for user information.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponse {

    private Long id;
    private String name;
    private String surname;
    private String email;
    private String post;
    private Role role;
    private String organizationName;
}
