package com.bloodlink.controllers;

import com.bloodlink.exceptions.CustomDuplicateException;
import com.bloodlink.service.UserService;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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


    @RolesAllowed(value = {"ADMIN"})
    @GetMapping
    public ResponseEntity<?> getUsers( @RequestParam String pattern,
                                      Pageable page) {
        return ResponseEntity.ok(userService.getAll(pattern, page));
    }

    @RolesAllowed(value = {"ADMIN"})
    @DeleteMapping
    public ResponseEntity<?> deleteUser(@RequestParam Long id) throws CustomDuplicateException {
        return ResponseEntity.ok(userService.delete(id));
    }
}
