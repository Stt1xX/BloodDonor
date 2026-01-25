package com.bloodlink.dto.request;

import com.bloodlink.entities.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request DTO for user registration.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegistrationCreateRequest {

    @NotBlank(message = "Поле не может быть пустым")
    private String name;

    @NotBlank(message = "Поле не может быть пустым")
    private String surname;

    @Email(message = "Некорректный email")
    private String email;

    @NotBlank(message = "Пароль не может быть пустым")
    @Size(min = 5, message = "Пароль должен содержать минимум 5 символов")
    private String password;

    private Role role;

    @NotBlank(message = "Поле не может быть пустым")
    private String post;

    private Long organizationId;
}
