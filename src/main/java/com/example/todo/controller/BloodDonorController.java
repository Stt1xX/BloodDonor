package com.example.todo.controller;

import com.example.todo.model.BloodDonor;
import com.example.todo.repository.BloodDonorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/donors")
@Tag(name = "Blood Donor API", description = "API для управления донорами крови")
public class BloodDonorController {

    @Autowired
    private BloodDonorRepository bloodDonorRepository;

    @GetMapping
    @Operation(summary = "Получить список всех доноров")
    public List<BloodDonor> getAllDonors() {
        return bloodDonorRepository.findAll();
    }

    @GetMapping("/available")
    @Operation(summary = "Получить список доступных доноров")
    public List<BloodDonor> getAvailableDonors() {
        return bloodDonorRepository.findByIsAvailable(true);
    }

    @GetMapping("/blood-type/{bloodType}")
    @Operation(summary = "Найти доноров по группе крови")
    public List<BloodDonor> getDonorsByBloodType(@PathVariable String bloodType) {
        return bloodDonorRepository.findByBloodType(bloodType);
    }

    @PostMapping
    @Operation(summary = "Добавить нового донора")
    public BloodDonor createDonor(@Valid @RequestBody BloodDonor donor) {
        return bloodDonorRepository.save(donor);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Обновить информацию о доноре")
    public ResponseEntity<BloodDonor> updateDonor(@PathVariable Long id, @Valid @RequestBody BloodDonor donorDetails) {
        return bloodDonorRepository.findById(id)
                .map(donor -> {
                    donor.setFullName(donorDetails.getFullName());
                    donor.setBloodType(donorDetails.getBloodType());
                    donor.setPhoneNumber(donorDetails.getPhoneNumber());
                    donor.setAvailable(donorDetails.isAvailable());
                    donor.setLastDonationDate(donorDetails.getLastDonationDate());
                    BloodDonor updatedDonor = bloodDonorRepository.save(donor);
                    return ResponseEntity.ok(updatedDonor);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить донора")
    public ResponseEntity<?> deleteDonor(@PathVariable Long id) {
        return bloodDonorRepository.findById(id)
                .map(donor -> {
                    bloodDonorRepository.delete(donor);
                    return ResponseEntity.ok().build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
} 