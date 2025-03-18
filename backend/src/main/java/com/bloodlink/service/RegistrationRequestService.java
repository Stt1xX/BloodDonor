package com.bloodlink.service;

import com.bloodlink.entities.DTOs.RegistrationRequestDTOfrom;
import com.bloodlink.entities.DTOs.RegistrationRequestDTOto;
import com.bloodlink.entities.RegistrationRequest;
import com.bloodlink.entities.User;
import com.bloodlink.entities.enums.Role;
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
    public String save(RegistrationRequestDTOfrom requestDTOfrom) {
        if (userService.getByEmail(requestDTOfrom.getEmail()) != null || registrationRequestRepository.findByEmail(requestDTOfrom.getEmail()).isPresent()) {
            throw new CustomDuplicateException("Пользователь или запрос с таким email уже существует");
        }
        System.out.println(userService.getByEmail(requestDTOfrom.getEmail()));
        System.out.println(registrationRequestRepository.findByEmail(requestDTOfrom.getEmail()).isPresent());
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
        if (registrationRequestRepository.countByRole(Role.ADMIN) == 0 && request.getRole() == Role.ADMIN) {
            registerUser(request);
            return "Администратор успешно создан!";
        } else {
            registrationRequestRepository.save(request);
            return "Заявка на создание пользователя успешно подана!";
        }
    }

    public Page<RegistrationRequestDTOto> getRequestsDto(String pattern, Pageable p) throws IllegalServiceArgumentException {
        Specification<RegistrationRequest> filters = Specification.where(!StringUtils.hasLength(pattern) ? null : RegistrationRequestsSpecs.nameLike(pattern))
                .or(!StringUtils.hasLength(pattern) ? null : RegistrationRequestsSpecs.surnameLike(pattern))
                .or(!StringUtils.hasLength(pattern) ? null : RegistrationRequestsSpecs.emailLike(pattern))
                .or(!StringUtils.hasLength(pattern) ? null : RegistrationRequestsSpecs.roleLike(pattern));

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
