package com.bloodlink.service;

import com.bloodlink.entities.BloodUnit;
import com.bloodlink.entities.DTOs.BloodUnitDTOto;
import com.bloodlink.entities.DTOs.OrganizationDTOfrom;
import com.bloodlink.entities.DTOs.OrganizationDTOto;
import com.bloodlink.entities.Organization;
import com.bloodlink.entities.User;
import com.bloodlink.entities.enums.OrganizationType;
import com.bloodlink.entities.enums.Role;
import com.bloodlink.entities.specifications.BloodUnitsSpecs;
import com.bloodlink.entities.specifications.OrganizationSpecs;
import com.bloodlink.exceptions.CustomDuplicateException;
import com.bloodlink.repositories.BloodUnitRepository;
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
public class BloodUnitsService {

    private final OrganizationRepository organizationRepository;
    private final BloodUnitRepository bloodUnitRepository;

    public Page<BloodUnitDTOto> getOrganizationBloodUnits(User employee, String type, String rhFactor,
                                                          Pageable page) {
        var org = employee.getOrganization();
        assert org.getType() == OrganizationType.BLOOD_BANK : "Expected blood bank employee as caller";


        Specification<BloodUnit> filters = Specification.where(!StringUtils.hasLength(type) ? null :
                        BloodUnitsSpecs.bloodTypeLike(type))
                .or(!StringUtils.hasLength(rhFactor) ? null : BloodUnitsSpecs.rhFactorLike(rhFactor));

        var units = bloodUnitRepository.getBloodUnitsByBloodBank(org, filters, page);
        return units.map(BloodUnitDTOto::convert);
    }


}
