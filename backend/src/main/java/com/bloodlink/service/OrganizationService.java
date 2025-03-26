package com.bloodlink.service;

import com.bloodlink.entities.DTOs.OrganizationDTOfrom;
import com.bloodlink.entities.DTOs.OrganizationDTOto;
import com.bloodlink.entities.Organization;
import com.bloodlink.entities.enums.OrganizationType;
import com.bloodlink.entities.enums.Role;
import com.bloodlink.entities.specifications.OrganizationSpecs;
import com.bloodlink.exceptions.CustomDuplicateException;
import com.bloodlink.repositories.BloodBankRepository;
import com.bloodlink.repositories.MedicalInstitutionRepository;
import com.bloodlink.repositories.OrganizationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class OrganizationService {

    private final OrganizationRepository organizationRepository;
    private final MedicalInstitutionRepository medicalInstitutionRepository;
    private final BloodBankRepository bloodBankRepository;

    public Page<OrganizationDTOto> getAll(String type, String pattern, Pageable page) {
        OrganizationType typeEnum = null;
        var role = Role.fromString(type);
        if (role != null) {
            typeEnum = switch (role) {
                case ADMIN -> null;
                case MEDICAL_EMPLOYEE -> OrganizationType.MEDICAL_INSTITUTION;
                case BLOOD_BANK_EMPLOYEE ->OrganizationType.BLOOD_BANK;
            };
        }
        Specification<Organization> filters = Specification.where(!StringUtils.hasLength(pattern) ? null :
                    OrganizationSpecs.nameLike(pattern))
                .or(OrganizationSpecs.addressLike(pattern))
                .or(OrganizationSpecs.phoneLike(pattern))
                .or(OrganizationSpecs.hasType(typeEnum));

        var p = organizationRepository.findAll(filters, page);
        return p.map(OrganizationDTOto::convert);
    }

    @Transactional
    public String save(OrganizationDTOfrom organizationDTOfrom) throws CustomDuplicateException {
        try {
            if (organizationDTOfrom.getType() == OrganizationType.BLOOD_BANK) {
                bloodBankRepository.save(organizationDTOfrom.getOrganization());
                return "Банк крови успешно добавлен";
            } else if  (organizationDTOfrom.getType() == OrganizationType.MEDICAL_INSTITUTION) {
                medicalInstitutionRepository.save(organizationDTOfrom.getOrganization());
                return "Медицинское учрежедние успешно добавлено";
            } else  {
                return "Некорректный тип организации";
            }
        } catch (DataIntegrityViolationException e) {
            throw new CustomDuplicateException("Заведение с таким именем уже существует");
        }
    }

    @Transactional
    public String update(OrganizationDTOfrom organizationDTOfrom) throws CustomDuplicateException {
        if (organizationDTOfrom.getId() == null) {
            throw new IllegalArgumentException("Для обновления организации не предоставлен id");
        }
        Optional<Organization> orgOpt = organizationRepository.findById(organizationDTOfrom.getId());
        if (orgOpt.isEmpty()) {
            throw new IllegalArgumentException("Организация с таким id не существует");
        }
        save(organizationDTOfrom);
        return "Информация о заведении обновлена";
    }

    @Transactional
    public String delete(Long id) throws CustomDuplicateException {
        Optional<Organization> orgOpt = organizationRepository.findById(id);
        if (orgOpt.isEmpty()) {
            throw new IllegalArgumentException("Организация с таким id уже существует");
        }
        organizationRepository.delete(orgOpt.get());
        return "Организация успешно удалена";
    }

    public Organization get(Long id) {
        return id == null ? null : organizationRepository.findById(id).orElse(null);
    }
}
