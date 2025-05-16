package org.nicetu.nicshop.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.nicetu.nicshop.domain.User;
import org.nicetu.nicshop.dto.JwtResponseDto;
import org.nicetu.nicshop.requests.AuthRequest;
import org.nicetu.nicshop.requests.RefreshTokenRequest;
import org.nicetu.nicshop.requests.RegisterRequest;
import org.nicetu.nicshop.security.jwt.JwtAuthentication;
import org.nicetu.nicshop.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@Tag(name = "Аутентификация", description = "API для регистрации, входа и управления токенами")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @Operation(
            summary = "Регистрация пользователя",
            description = "Создает нового пользователя в системе"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Пользователь успешно зарегистрирован"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные")
    })
    @PostMapping("/register")
    public ResponseEntity<User> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.registerUser(request));
    }

    @Operation(
            summary = "Аутентификация пользователя",
            description = "Возвращает пару JWT токенов (access + refresh) по логину и паролю"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешная аутентификация"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные"),
            @ApiResponse(responseCode = "401", description = "Неверные учетные данные")
    })
    @PostMapping("/login")
    public ResponseEntity<JwtResponseDto> login(@Valid @RequestBody AuthRequest request) {
        return ResponseEntity.ok(authService.authenticateUser(request));
    }

    @Operation(
            summary = "Обновление токенов",
            description = "Генерирует новую пару токенов по валидному refresh-токену"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Токены успешно обновлены"),
            @ApiResponse(responseCode = "400", description = "Некорректный refresh-токен")
    })
    @PostMapping("/refresh")
    public ResponseEntity<JwtResponseDto> refreshToken(@Valid @RequestBody RefreshTokenRequest request) {
        return ResponseEntity.ok(authService.refreshTokens(request));
    }

    @Operation(
            summary = "Выход из системы",
            description = "Аннулирует все refresh-токены пользователя"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешный выход"),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    })
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(JwtAuthentication authentication) {
        authService.deleteAllById(authentication.getUserId());
        return ResponseEntity.ok().build();
    }
}

