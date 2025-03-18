package com.bloodlink.controllers;

import com.bloodlink.entities.BankEmployee;
import com.bloodlink.entities.DTOs.BloodUnitDto;
import com.bloodlink.repositories.BankEmployeeRepository;
import com.bloodlink.repositories.BloodUnitRepository;
import com.bloodlink.service.ResourceUtilsService;
import jakarta.annotation.security.RolesAllowed;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/api/blood_units")
@RequiredArgsConstructor
public class BloodRequestResource {

    private final ResourceUtilsService resourceUtilsService;
    private final BankEmployeeRepository bankEmployeeRepository;
    private final BloodUnitRepository bloodUnitRepository;

    @RolesAllowed(value = {"BANK_EMPLOYEE", "ADMIN"})
    @GetMapping
    public Page<BloodUnitDto> getBloodUnits(@RequestParam String pattern, Pageable p) {
        var caller = resourceUtilsService.getCaller();
        Optional<BankEmployee> emplOpt = bankEmployeeRepository.findById(caller.getId());
        if (emplOpt.isPresent()) {
            throw new IllegalArgumentException("Вызывающий пользователь не является работником банка");
        }
        // тут нужна спецификация по pattern
        var bank = emplOpt.get().getBloodBank();
        var page = bloodUnitRepository.getBloodUnitsByBloodBank(bank, p);
        return page.map(BloodUnitDto::convert);
    }

    @RolesAllowed(value = {"BANK_EMPLOYEE"})
    @PostMapping
    public ResponseEntity<?> addBloodUnit(@RequestBody BloodUnitDto dto) {
        var caller = resourceUtilsService.getCaller();
        Optional<BankEmployee> emplOpt = bankEmployeeRepository.findById(caller.getId());
        if (emplOpt.isPresent()) {
            throw new IllegalArgumentException("Вызывающий пользователь не является работником банка");
        }
        var bank = emplOpt.get().getBloodBank();
        var entity = BloodUnitDto.convertFrom(dto);
        entity.setBloodBank(bank);
        bloodUnitRepository.save(entity);
        return ResponseEntity.ok("Запись о крови успешно добавлена!");
    }

    @RolesAllowed(value = {"BANK_EMPLOYEE"})
    @DeleteMapping
    public ResponseEntity<?> removeBloodUnit(@RequestBody BloodUnitDto dto) {
        var caller = resourceUtilsService.getCaller();
        Optional<BankEmployee> emplOpt = bankEmployeeRepository.findById(caller.getId());
        if (emplOpt.isPresent()) {
            throw new IllegalArgumentException("Вызывающий пользователь не является работником банка");
        }
        var bank = emplOpt.get().getBloodBank();
        var unitOpt = bloodUnitRepository.findById(dto.getId());
        if (unitOpt.isPresent()) {
            throw new IllegalArgumentException("Запись о крови не найдена");
        }
        var unit = unitOpt.get();
        if (unit.getBloodBank().getId() != bank.getId()) {
            throw new IllegalArgumentException("Найдена запись о крови другой организации");
        }
        bloodUnitRepository.delete(unit);
        return ResponseEntity.ok("Запись о крови успешно удалена!");
    }

}
