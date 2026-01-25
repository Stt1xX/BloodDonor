package com.bloodlink.controllers;

import com.bloodlink.dto.response.UserResponse;
import com.bloodlink.entities.User;
import com.bloodlink.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<Map<String, String>> getCurrentUserInfo() {
        log.debug("Getting current user info");
        User currentUser = userService.getCurrentUser();
        return ResponseEntity.ok(Map.of(
                "name", currentUser.getName(),
                "surname", currentUser.getSurname(),
                "role", currentUser.getRole().toString()
        ));
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping
    public ResponseEntity<Page<UserResponse>> getUsers(
            @RequestParam String pattern,
            Pageable page) {
        log.debug("Getting users with pattern={}", pattern);
        return ResponseEntity.ok(userService.getAll(pattern, page));
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        log.info("Deleting user id={}", id);
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
