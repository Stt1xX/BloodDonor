package com.bloodlink.controllers;

import com.bloodlink.entities.DTOs.BloodUnitDTOto;
import com.bloodlink.repositories.BloodUnitRepository;
import com.bloodlink.service.BloodUnitsService;
import com.bloodlink.service.ResourceUtilsService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/api/blood_units")
@RequiredArgsConstructor
public class BloodUnitResource {

    private final BloodUnitRepository bloodUnitRepository;
    private final ResourceUtilsService resourceUtilsService;
    private final BloodUnitsService bloodUnitsService;

    @PreAuthorize("hasAnyAuthority('BANK_EMPLOYEE')")
    @GetMapping
    public Page<BloodUnitDTOto> getBloodUnits(@RequestParam String bloodType, @RequestParam String rhFactor,
                                              Pageable p) {
        var caller = resourceUtilsService.getCaller();
        return bloodUnitsService.getOrganizationBloodUnits(caller, bloodType, rhFactor, p);
    }
//
//    @RolesAllowed(value = {"BANK_EMPLOYEE"})
//    @PostMapping
//    public ResponseEntity<?> addBloodUnit(@RequestBody BloodUnitDTOto dto) {
//        var caller = resourceUtilsService.getCaller();
//        Optional<BankEmployee> emplOpt = bankEmployeeRepository.findById(caller.getId());
//        if (emplOpt.isPresent()) {
//            throw new IllegalArgumentException("Вызывающий пользователь не является работником банка");
//        }
//        var bank = emplOpt.get().getBloodBank();
//        var entity = BloodUnitDTOto.convertFrom(dto);
//        entity.setBloodBank(bank);
//        bloodUnitRepository.save(entity);
//        return ResponseEntity.ok("Запись о крови успешно добавлена!");
//    }
//
//    @RolesAllowed(value = {"BANK_EMPLOYEE"})
//    @DeleteMapping
//    public ResponseEntity<?> removeBloodUnit(@RequestBody BloodUnitDTOto dto) {
//        var caller = resourceUtilsService.getCaller();
//        Optional<BankEmployee> emplOpt = bankEmployeeRepository.findById(caller.getId());
//        if (emplOpt.isPresent()) {
//            throw new IllegalArgumentException("Вызывающий пользователь не является работником банка");
//        }
//        var bank = emplOpt.get().getBloodBank();
//        var unitOpt = bloodUnitRepository.findById(dto.getId());
//        if (unitOpt.isPresent()) {
//            throw new IllegalArgumentException("Запись о крови не найдена");
//        }
//        var unit = unitOpt.get();
//        if (unit.getBloodBank().getId() != bank.getId()) {
//            throw new IllegalArgumentException("Найдена запись о крови другой организации");
//        }
//        bloodUnitRepository.delete(unit);
//        return ResponseEntity.ok("Запись о крови успешно удалена!");
//    }

}
