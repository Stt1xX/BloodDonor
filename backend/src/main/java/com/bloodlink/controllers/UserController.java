package com.bloodlink.controllers;

import com.bloodlink.dto.response.UserResponse;
import com.bloodlink.entities.User;
import com.bloodlink.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Users", description = "API для управления пользователями")
public class UserController {

    private final UserService userService;

    @Operation(summary = "Получить информацию о текущем пользователе")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Успешно"),
            @ApiResponse(responseCode = "401", description = "Не авторизован")
    })
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

    @Operation(
            summary = "Получить список пользователей",
            description = "Возвращает пагинированный список пользователей с поиском по ФИО"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Успешно"),
            @ApiResponse(responseCode = "403", description = "Нет доступа (требуется роль ADMIN)")
    })
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping
    public ResponseEntity<Page<UserResponse>> getUsers(
            @Parameter(description = "Поисковый паттерн по ФИО") @RequestParam String pattern,
            Pageable page) {
        log.debug("Getting users with pattern={}", pattern);
        return ResponseEntity.ok(userService.getAll(pattern, page));
    }

    @Operation(summary = "Удалить пользователя")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Пользователь удалён"),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    })
    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(
            @Parameter(description = "ID пользователя") @PathVariable Long id) {
        log.info("Deleting user id={}", id);
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
