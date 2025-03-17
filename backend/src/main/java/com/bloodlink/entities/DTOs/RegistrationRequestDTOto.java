package com.bloodlink.entities.DTOs;

import com.bloodlink.entities.RegistrationRequest;
import com.bloodlink.entities.enums.Role;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RegistrationRequestDTOto {

    private Long id;

    private String name;

    private String surname;

    private String email;

    private Role role;

    private LocalDateTime createdAt;

    private OrganizationDTOto organization;

    public RegistrationRequestDTOto(Long id, String name, String surname, String email, Role role,
                                    OrganizationDTOto organization, LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.role = role;
        this.organization = organization;
        this.createdAt = createdAt;
    }

    public static RegistrationRequestDTOto convert(RegistrationRequest entity) {
        return new RegistrationRequestDTOto(entity.getId(), entity.getName(), entity.getSurname(), entity.getEmail(),
                entity.getRole(), OrganizationDTOto.convert(entity.getBloodBank() == null ?
                entity.getInstitution() : entity.getBloodBank()), entity.getCreatedAt());
    }


}

