package com.bloodlink.controllers;

import com.bloodlink.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class UserController {


    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/info")
    public Map<String, String> getUserInfo() {
        Map<String, String> response = new HashMap<>();
        if (userService.getCurrentUser().isPresent()) {
            var currentUser = userService.getCurrentUser().get();
            response.put("name", currentUser.getName());
            response.put("surname", currentUser.getSurname());
            response.put("role", currentUser.getRole().toString());
        }
        return response;
    }
}
