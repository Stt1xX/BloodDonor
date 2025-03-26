package com.bloodlink.service;

import com.bloodlink.entities.DTOs.RegistrationRequestDTOfrom;
import com.bloodlink.entities.DTOs.RegistrationRequestDTOto;
import com.bloodlink.entities.Organization;
import com.bloodlink.entities.RegistrationRequest;
import com.bloodlink.entities.User;
import com.bloodlink.entities.enums.OrganizationType;
import com.bloodlink.entities.enums.Role;
import com.bloodlink.entities.specifications.RegistrationRequestsSpecs;
import com.bloodlink.exceptions.CustomDuplicateException;
import com.bloodlink.exceptions.IllegalServiceArgumentException;
import com.bloodlink.repositories.RegistrationRequestRepository;
import com.bloodlink.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
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
    private final RegistrationRequestRepository registrationRequestRepository;
    private final UserService userService;
    private final UserRepository userRepository;
    private final OrganizationService organizationService;

    @Transactional
    public String save(RegistrationRequestDTOfrom requestDTOfrom) {
        if (userService.getByEmail(requestDTOfrom.getEmail()) != null || registrationRequestRepository.findByEmail(requestDTOfrom.getEmail()).isPresent()) {
            throw new CustomDuplicateException("Пользователь или запрос с таким email уже существует");
        }
        RegistrationRequest request = requestDTOfrom.convertToRegistrationRequest();
        if (requestDTOfrom.getRole() != Role.ADMIN &&  organizationService.get(requestDTOfrom.getOrganizationId()) == null) {
            throw new IllegalServiceArgumentException("Не указан id организации");
        }
        Organization org;
        if ((org = organizationService.get(requestDTOfrom.getOrganizationId())) != null)
            if (org.getType() == OrganizationType.BLOOD_BANK &&
                requestDTOfrom.getRole() != Role.BANK_EMPLOYEE ||
                org.getType() == OrganizationType.MEDICAL_INSTITUTION &&
                requestDTOfrom.getRole() != Role.MEDICAL_EMPLOYEE
            ){
                throw new IllegalServiceArgumentException("Ваша роль не соответствует заведению, которые вы указали");
            }
        request.setOrganization(organizationService.get(requestDTOfrom.getOrganizationId()));
        request.setPassword(encoder.encode(request.getPassword()));
        if (userRepository.countByRole(Role.ADMIN) == 0 && request.getRole() == Role.ADMIN) {
            registerUser(request);
            return "Администратор успешно создан!";
        } else {
            registrationRequestRepository.save(request);
            return "Заявка на создание пользователя успешно подана!";
        }
    }

    public Page<RegistrationRequestDTOto> getRequestsDto(String pattern, Pageable p) {
        Specification<RegistrationRequest> filters = Specification.where(!StringUtils.hasLength(pattern) ? null :
                    RegistrationRequestsSpecs.nameLike(pattern))
                .or(RegistrationRequestsSpecs.surnameLike(pattern))
                .or(RegistrationRequestsSpecs.emailLike(pattern))
                .or(RegistrationRequestsSpecs.roleLike(pattern));

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
        user.setPost(req.getPost());
        userRepository.save(user);
    }
}
