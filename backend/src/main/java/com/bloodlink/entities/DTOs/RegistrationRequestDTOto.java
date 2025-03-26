package com.bloodlink.entities.DTOs;

import com.bloodlink.entities.RegistrationRequest;
import com.bloodlink.entities.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class RegistrationRequestDTOto {

    private Long id;

    private String name;

    private String surname;

    private String email;

    private Role role;

    private LocalDateTime createdAt;

    private String organizationName;

    public static RegistrationRequestDTOto convert(RegistrationRequest entity) {
        return new RegistrationRequestDTOto(entity.getId(), entity.getName(), entity.getSurname(),
                entity.getEmail(), entity.getRole(), entity.getCreatedAt(),
                entity.getOrganization() == null ? null : entity.getOrganization().getName());
    }


}

