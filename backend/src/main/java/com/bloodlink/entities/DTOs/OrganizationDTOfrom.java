package com.bloodlink.entities.DTOs;

import com.bloodlink.entities.Organization;
import com.bloodlink.entities.enums.OrganizationType;
import com.bloodlink.validators.annotaions.WorkTimeCheckAnnotation;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
@WorkTimeCheckAnnotation(hoursStart = "hoursFrom", hoursEnd = "hoursTo", minutesStart = "minutesFrom", minutesEnd = "minutesTo")
public class OrganizationDTOfrom {
    private Long id;
    private OrganizationType type;
    @NotNull(message = "Название не может быть пустым")
    private String name;
    @NotNull(message = "Адрес не может быть пустым")
    private String address;
    @NotNull(message = "Телефон не может быть пустым")
    @Pattern(regexp = "^(\\+7|8)\\d{10}$",
            message = "Некорректный номер телефона. Ожидается мобильный (+7XXXXXXXXXX или 8XXXXXXXXXX)")
    private String phone;
    @NotNull(message = "Время работы не может быть пустым")
    private Integer hoursFrom;
    @NotNull(message = "Время работы не может быть пустым")
    private Integer hoursTo;
    @NotNull(message = "Время работы не может быть пустым")
    private Integer minutesFrom;
    @NotNull(message = "Время работы не может быть пустым")
    private Integer minutesTo;

    public Organization convert() {
        try {
            Organization e = new Organization();
            e.setName(name);
            e.setAddress(address);
            e.setPhone(phone);
            e.setHoursFrom(hoursFrom);
            e.setHoursTo(hoursTo);
            e.setMinutesFrom(minutesFrom);
            e.setMinutesTo(minutesTo);
            e.setId(id);
            e.setType(type);
            return e;
        } catch (Exception ex) {
            return null;
        }
    }
}
