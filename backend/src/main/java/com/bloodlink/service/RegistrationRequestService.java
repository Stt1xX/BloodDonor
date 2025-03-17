package com.bloodlink.service;

import com.bloodlink.entities.DTOs.RegistrationRequestDTOfrom;
import com.bloodlink.entities.DTOs.RegistrationRequestDTOto;
import com.bloodlink.entities.RegistrationRequest;
import com.bloodlink.entities.User;
import com.bloodlink.entities.specifications.RegistrationRequestsSpecs;
import com.bloodlink.exceptions.CustomDuplicateException;
import com.bloodlink.exceptions.IllegalServiceArgumentException;
import com.bloodlink.repositories.RegistrationRequestRepository;
import com.bloodlink.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class RegistrationRequestService {

    private final PasswordEncoder encoder;
    private final BloodBankService bloodBankService;
    private final MedicalInstitutionService medicalInstitutionService;
    private final RegistrationRequestRepository registrationRequestRepository;
    private final UserService userService;
    private final UserRepository userRepository;

    @Transactional
    public void save(RegistrationRequestDTOfrom requestDTOfrom) {
        try {
            if (userService.getByEmail(requestDTOfrom.getEmail()) != null) {
                throw new CustomDuplicateException("Пользователь с таким email уже существует");
            }
            RegistrationRequest request = requestDTOfrom.convertToRegistrationRequest();
            switch (requestDTOfrom.getRole()) {
                case MEDICAL_EMPLOYEE:
                    if (requestDTOfrom.getOrganizationId() == null || medicalInstitutionService.get(requestDTOfrom.getOrganizationId()) == null) {
                        throw new IllegalArgumentException("Не указан id медицинского учреждения");
                    }
                    request.setInstitution(medicalInstitutionService.get(requestDTOfrom.getOrganizationId()));
                    break;
                case BLOOD_BANK_EMPLOYEE:
                    if (requestDTOfrom.getOrganizationId() == null || bloodBankService.get(requestDTOfrom.getOrganizationId()) == null) {
                        throw new IllegalArgumentException("Не указан id банка крови");
                    }
                    request.setBloodBank(bloodBankService.get(requestDTOfrom.getOrganizationId()));
                    break;
            }
            request.setPassword(encoder.encode(request.getPassword()));
            registrationRequestRepository.save(request);
        } catch (DataIntegrityViolationException e) {
            throw new CustomDuplicateException("Запрос с таким email уже создан");
        }
    }

    public Page<RegistrationRequestDTOto> getRequestsDto(String name, String surname, String email, String role, Pageable p) throws IllegalServiceArgumentException {
        Specification<RegistrationRequest> filters = Specification.where(!StringUtils.hasLength(name) ? null : RegistrationRequestsSpecs.nameLike(name))
                .and(!StringUtils.hasLength(surname) ? null : RegistrationRequestsSpecs.surnameLike(surname))
                .and(!StringUtils.hasLength(email) ? null : RegistrationRequestsSpecs.emailLike(email))
                .and(!StringUtils.hasLength(role) ? null : RegistrationRequestsSpecs.roleLike(role));

        var requests = registrationRequestRepository.findAll(filters, p);
        return requests.map(RegistrationRequestDTOto::convert);
    }

    public void approveRequest(Long id) {
        var reqOpt = registrationRequestRepository.findById(id);
        if (reqOpt.isEmpty()) {
            throw new IllegalArgumentException("Указан неверный id запроса");
        }
        var request = reqOpt.get();
        registerUser(request);
        registrationRequestRepository.delete(reqOpt.get());
    }

    public void rejectRequest(Long id) {
        var reqOpt = registrationRequestRepository.findById(id);
        if (reqOpt.isEmpty()) {
            throw new IllegalArgumentException("Указан неверный id запроса");
        }
        registrationRequestRepository.delete(reqOpt.get());
    }

    private void registerUser(RegistrationRequest req) {
        var user = new User();
        user.setName(req.getName());
        user.setSurname(req.getSurname());
        user.setEmail(req.getEmail());
        user.setPassword(req.getPassword());
        user.setRole(req.getRole());
        userRepository.save(user);
    }
}
