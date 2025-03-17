package com.bloodlink.entities.DTOs;

import com.bloodlink.entities.BloodBank;
import com.bloodlink.entities.MedicalInstitution;
import com.bloodlink.entities.Organization;
import com.bloodlink.entities.enums.OrganizationType;
import com.bloodlink.validators.annotaions.WorkTimeCheckAnnotation;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
@WorkTimeCheckAnnotation(hoursStart = "hoursFrom", hoursEnd = "hoursTo", minutesStart = "minutesFrom", minutesEnd = "minutesTo")
public class OrganizationDTOfrom {
    private OrganizationType type;
    @NotNull(message = "Название не может быть пустым")
    private String name;
    @NotNull(message = "Адрес не может быть пустым")
    private String address;
    @NotNull(message = "Телефон не может быть пустым")
    @Pattern(regexp = "^(\\+7|8)\\d{10}$|^(\\(?\\d{3,5}\\)?[\\s-]?\\d{1,3}[\\s-]?\\d{2}[\\s-]?\\d{2})$",
            message = "Некорректный номер телефона. Ожидается мобильный (+7XXXXXXXXXX или 8XXXXXXXXXX) " +
                    "или домашний (XXX-XX-XX или (XXX) XXX-XX-XX)")
    private String phone;
    @NotNull(message = "Время работы не может быть пустым")
    private Integer hoursFrom;
    @NotNull(message = "Время работы не может быть пустым")
    private Integer hoursTo;
    @NotNull(message = "Время работы не может быть пустым")
    private Integer minutesFrom;
    @NotNull(message = "Время работы не может быть пустым")
    private Integer minutesTo;

    public <E extends Organization> E getOrganization() {
        try {
            Organization e = switch (type) {
                case BLOOD_BANK -> new BloodBank();
                case MEDICAL_INSTITUTION -> new MedicalInstitution();
            };
            e.setName(name);
            e.setAddress(address);
            e.setPhone(phone);
            e.setHoursFrom(hoursFrom);
            e.setHoursTo(hoursTo);
            e.setMinutesFrom(minutesFrom);
            e.setMinutesTo(minutesTo);
            return (E) e;
        } catch (Exception ex) {
            return null;
        }
    }
}
