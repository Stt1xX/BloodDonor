package com.bloodlink.entities.DTOs;

import com.bloodlink.entities.User;
import com.bloodlink.entities.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserDTOto {

    private Long id;
    private String name;
    private String surname;
    private String email;
    private String post;
    private Role role;
    private String organizationName;

    public static UserDTOto convert(User user){
        return new UserDTOto(user.getId(), user.getName(),
                user.getSurname(), user.getEmail(), user.getPost(), user.getRole(),
                user.getOrganization() != null ? user.getOrganization().getName() : "");
    }
}
