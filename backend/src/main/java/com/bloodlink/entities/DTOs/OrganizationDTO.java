package com.bloodlink.entities.DTOs;

import com.bloodlink.entities.BloodBank;
import com.bloodlink.entities.MedicalInstitution;
import com.bloodlink.entities.enums.OrganizationType;
import lombok.Data;

@Data
public class OrganizationDTO {
    private OrganizationType type;
    private String name;
    private String address;
    private String phone;
    private Integer hoursFrom;
    private Integer hoursTo;
    private Integer minutesFrom;
    private Integer minutesTo;

    public BloodBank getBloodBank() {
        BloodBank bloodBank = new BloodBank();
        bloodBank.setName(name);
        bloodBank.setAddress(address);
        bloodBank.setPhone(phone);
        bloodBank.setWorkTime(getWorkTime());
        return bloodBank;
    }

    public MedicalInstitution getMedicalInstitution() {
        MedicalInstitution medicalInstitution = new MedicalInstitution();
        medicalInstitution.setName(name);
        medicalInstitution.setAddress(address);
        medicalInstitution.setPhone(phone);
        medicalInstitution.setWorkTime(getWorkTime());
        return medicalInstitution;
    }

    public String getWorkTime() {
        return hoursFrom + ":" + minutesFrom + " - " + hoursTo + ":" + minutesTo;
    }

 }
