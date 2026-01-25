package com.bloodlink.service;

import com.bloodlink.dto.mapper.RegistrationRequestMapper;
import com.bloodlink.dto.request.RegistrationCreateRequest;
import com.bloodlink.dto.response.RegistrationRequestResponse;
import com.bloodlink.entities.Organization;
import com.bloodlink.entities.RegistrationRequest;
import com.bloodlink.entities.User;
import com.bloodlink.entities.enums.OrganizationType;
import com.bloodlink.entities.enums.Role;
import com.bloodlink.entities.specifications.RegistrationRequestsSpecs;
import com.bloodlink.exceptions.BusinessException;
import com.bloodlink.exceptions.ConflictException;
import com.bloodlink.exceptions.ResourceNotFoundException;
import com.bloodlink.repositories.RegistrationRequestRepository;
import com.bloodlink.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
@Slf4j
public class RegistrationRequestService {

    private final PasswordEncoder encoder;
    private final RegistrationRequestRepository registrationRequestRepository;
    private final UserService userService;
    private final UserRepository userRepository;
    private final OrganizationService organizationService;
    private final RegistrationRequestMapper registrationRequestMapper;

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public RegistrationRequest save(RegistrationCreateRequest dto) {
        log.info("Processing registration request for email={}", dto.getEmail());
        
        validateEmailUniqueness(dto.getEmail());
        validateOrganization(dto);
        
        RegistrationRequest request = registrationRequestMapper.toEntity(dto);
        request.setOrganization(organizationService.get(dto.getOrganizationId()));
        request.setPassword(encoder.encode(dto.getPassword()));
        
        if (userRepository.countByRole(Role.ADMIN) == 0 && request.getRole() == Role.ADMIN) {
            registerUser(request);
            log.info("First admin created: {}", dto.getEmail());
            return request;
        } else {
            RegistrationRequest saved = registrationRequestRepository.save(request);
            log.info("Registration request created id={}", saved.getId());
            return saved;
        }
    }

    public Page<RegistrationRequestResponse> getRequestsDto(String pattern, Pageable p) {
        Specification<RegistrationRequest> filters = Specification.where(!StringUtils.hasLength(pattern) ? null :
                    RegistrationRequestsSpecs.nameLike(pattern))
                .or(RegistrationRequestsSpecs.surnameLike(pattern))
                .or(RegistrationRequestsSpecs.emailLike(pattern))
                .or(RegistrationRequestsSpecs.roleLike(pattern));

        var requests = registrationRequestRepository.findAll(filters, p);
        return requests.map(registrationRequestMapper::toResponse);
    }

    @Transactional
    public void approveRequest(Long id) {
        log.info("Approving registration request id={}", id);
        RegistrationRequest request = findRequestById(id);
        registerUser(request);
        registrationRequestRepository.delete(request);
        log.info("Registration request id={} approved, user created", id);
    }

    @Transactional
    public void rejectRequest(Long id) {
        log.info("Rejecting registration request id={}", id);
        RegistrationRequest request = findRequestById(id);
        registrationRequestRepository.delete(request);
        log.info("Registration request id={} rejected", id);
    }

    // ==================== Private Helper Methods ====================

    private RegistrationRequest findRequestById(Long id) {
        return registrationRequestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Запрос на регистрацию не найден: " + id));
    }

    private void validateEmailUniqueness(String email) {
        if (userService.findByEmail(email).isPresent() ||
                registrationRequestRepository.findByEmail(email).isPresent()) {
            throw new ConflictException("Пользователь или запрос с таким email уже существует");
        }
    }

    private void validateOrganization(RegistrationCreateRequest dto) {
        if (dto.getRole() == Role.ADMIN) {
            return; // Админу не нужна организация
        }
        
        Organization org = organizationService.get(dto.getOrganizationId());
        if (org == null) {
            throw new BusinessException("Не указан id организации");
        }
        
        boolean roleMatchesOrg = 
                (org.getType() == OrganizationType.BLOOD_BANK && dto.getRole() == Role.BANK_EMPLOYEE) ||
                (org.getType() == OrganizationType.MEDICAL_INSTITUTION && dto.getRole() == Role.MEDICAL_EMPLOYEE);
        
        if (!roleMatchesOrg) {
            throw new BusinessException("Ваша роль не соответствует заведению, которое вы указали");
        }
    }

    private void registerUser(RegistrationRequest req) {
        var user = new User();
        user.setName(req.getName());
        user.setSurname(req.getSurname());
        user.setEmail(req.getEmail());
        user.setPassword(req.getPassword());
        user.setRole(req.getRole());
        user.setPost(req.getPost());
        user.setIsDeleted(false);
        user.setOrganization(req.getOrganization());
        userRepository.save(user);
    }
}
