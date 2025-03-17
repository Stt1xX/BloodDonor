package com.bloodlink.service;

import com.bloodlink.entities.DTOs.RegistrationRequestDTOto;
import com.bloodlink.entities.RegistrationRequest;
import com.bloodlink.exceptions.CustomDuplicateException;
import com.bloodlink.repositories.RegistrationRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RegistrationRequestService {

    private final PasswordEncoder encoder;
    private final BloodBankService bloodBankService;
    private final MedicalInstitutionService medicalInstitutionService;
    private final RegistrationRequestRepository registrationRequestRepository;
    private final UserService userService;

    @Transactional
    public void save(RegistrationRequestDTOto requestDTOto) {
        try{
            if (userService.getByEmail(requestDTOto.getEmail()) != null){
                throw new CustomDuplicateException("Пользователь с таким email уже существует");
            }
            RegistrationRequest request = requestDTOto.convertToRegistrationRequest();
            switch (requestDTOto.getRole()) {
                case MEDICAL_EMPLOYEE:
                    if (requestDTOto.getOrganizationId() == null || medicalInstitutionService.get(requestDTOto.getOrganizationId()) == null) {
                        throw new IllegalArgumentException("Не указан id медицинского учреждения");
                    }
                    request.setInstitution(medicalInstitutionService.get(requestDTOto.getOrganizationId()));
                    break;
                case BLOOD_BANK_EMPLOYEE:
                    if (requestDTOto.getOrganizationId() == null || bloodBankService.get(requestDTOto.getOrganizationId()) == null) {
                        throw new IllegalArgumentException("Не указан id банка крови");
                    }
                    request.setBloodBank(bloodBankService.get(requestDTOto.getOrganizationId()));
                    break;
            }
            request.setPassword(encoder.encode(request.getPassword()));
            registrationRequestRepository.save(request);
        } catch (DataIntegrityViolationException e) {
            throw new CustomDuplicateException("Запрос с таким email уже создан");
        }
    }
}
