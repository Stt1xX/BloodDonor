package com.bloodlink.controllers;

import com.bloodlink.entities.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.bloodlink.service.UserService;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/get_user_info")
    public ResponseEntity<?> getUsername() {
        return userService.getUserInfo();
    }

    @PostMapping("/add")
    public ResponseEntity<?> addNewUser(@RequestBody User user) {
        return userService.addUser(user);
    }

    @PostMapping("/delete/{id}")
    public void deleteUser(@PathVariable int id) {
        userService.deleteUser(id);
    }
}
