package com.bloodlink.controllers;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class MainController {

    @GetMapping({"/login", "/registration", "/bank_employee/main",
            "/admin/main", "/medical_employee/main"})
    public String mainPage() {
        return "forward:/index.html";
    }

    @GetMapping({"/error", "/error/**"})
    public String errorPage() {
        return "forward:/index.html";
    }
}
