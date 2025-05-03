package org.nicetu.nicshop.service;


import io.jsonwebtoken.Claims;
import jakarta.transaction.Transactional;
import org.nicetu.nicshop.domain.RefreshToken;
import org.nicetu.nicshop.domain.Role;
import org.nicetu.nicshop.domain.User;
import org.nicetu.nicshop.dto.JwtResponseDto;
import org.nicetu.nicshop.repository.RefreshTokenRepo;
import org.nicetu.nicshop.repository.UserRepository;
import org.nicetu.nicshop.requests.AuthRequest;
import org.nicetu.nicshop.requests.RefreshTokenRequest;
import org.nicetu.nicshop.requests.RegisterRequest;
import org.nicetu.nicshop.security.jwt.JwtProvider;
import org.nicetu.nicshop.security.jwt.TokenResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashSet;
import java.util.Set;

@Service
public class AuthService {
    private final UserServiceImpl userService;
    private final RefreshTokenRepo refreshTokenRepo;
    private final UserRepository userRepo;
    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthService(UserServiceImpl userService,
                       RefreshTokenRepo refreshTokenRepo,
                       UserRepository userRepo,
                       JwtProvider jwtProvider,
                       PasswordEncoder passwordEncoder
    ) {
        this.userService = userService;
        this.refreshTokenRepo = refreshTokenRepo;
        this.userRepo = userRepo;
        this.jwtProvider = jwtProvider;
        this.passwordEncoder = passwordEncoder;
    }

    public JwtResponseDto authenticateUser(AuthRequest authRequest) {
        User user = userRepo.findByEmail(authRequest.getUserEmail());

        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Пользователя с таким email не существует");
        }

        if (!passwordEncoder.matches(authRequest.getUserPassword(), user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "Неверный пароль");
        }

        return generateJwtResponse(user, new RefreshToken(user));
    }

    @Transactional
    public User registerUser(RegisterRequest request) {
        String newEmail = request.getUserEmail();
        if (userRepo.existsByEmail(newEmail)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Пользователь с таким email уже существует");
        }

        User user = new User();
        user.setEmail(request.getUserEmail());
        user.setPassword(passwordEncoder.encode(request.getUserPassword()));
        user.setUsername(request.getFirstName() + " " + request.getLastName());
        user.setRoles(new HashSet<>(Set.of(Role.CLIENT)));
        userRepo.save(user);

        return user;
    }

    @Transactional
    public JwtResponseDto refreshTokens(RefreshTokenRequest request) {
        String oldRefreshToken = request.getRefreshToken();

        if (jwtProvider.validateRefreshToken(oldRefreshToken)) {
            RefreshToken refreshToken = refreshTokenRepo.findByToken(oldRefreshToken).orElseThrow(
                    () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Refresh-токен не найден"));

            Claims claims = jwtProvider.getRefreshClaims(oldRefreshToken);
            Long userId = Long.valueOf(claims.getSubject());
            User user = userService.getUserById(userId);
            return generateJwtResponse(user, refreshToken);
        }

        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Некорректный refresh-токен");
    }

    private JwtResponseDto generateJwtResponse(User user, RefreshToken refreshToken) {
        TokenResponse access = jwtProvider.generateAccessToken(user);
        TokenResponse refresh = jwtProvider.generateRefreshToken(user);
        refreshToken.setToken(refresh.getToken());
        refreshTokenRepo.save(refreshToken);

        return new JwtResponseDto(access, refresh);
    }

    @Transactional
    public void deleteAllById(Long id) {
        User user = userService.getUserById(id);
        refreshTokenRepo.deleteAllByUser(user);
    }
}
