package com.example.code.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class DonorController {

    @GetMapping("/donors")
    public String[] getAllDonors() {
        return new String[]{"Fugu Fish", "reloading!"};
    }
} 