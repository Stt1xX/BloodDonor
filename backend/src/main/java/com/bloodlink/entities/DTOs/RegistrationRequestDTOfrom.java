package com.bloodlink.entities.DTOs;

import com.bloodlink.entities.RegistrationRequest;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import com.bloodlink.entities.enums.Role;

@Data
public class RegistrationRequestDTOfrom {

    @NotBlank(message = "Поле не может быть пустым")
    private String name;

    @NotBlank(message = "Поле не может быть пустым")
    private String surname;

    @Email(message = "Некорректный email")
    private String email;

    @NotBlank
    @Size(min = 5, message = "Пароль должен содержать минимум 5 символов")
    private String password;

    private Role role;

    private Long organizationId;


    // !!! WITHOUT ID of organization !!!
    public RegistrationRequest convertToRegistrationRequest() {
        RegistrationRequest registrationRequest = new RegistrationRequest();
        registrationRequest.setName(this.name);
        registrationRequest.setSurname(this.surname);
        registrationRequest.setEmail(this.email);
        registrationRequest.setPassword(this.password);
        registrationRequest.setRole(this.role);
        return registrationRequest;
    }
}

