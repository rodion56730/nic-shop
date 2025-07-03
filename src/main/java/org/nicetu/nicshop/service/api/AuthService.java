package org.nicetu.nicshop.service.api;

import org.nicetu.nicshop.domain.User;
import org.nicetu.nicshop.dto.JwtResponseDto;
import org.nicetu.nicshop.requests.AuthRequest;
import org.nicetu.nicshop.requests.RefreshTokenRequest;
import org.nicetu.nicshop.requests.RegisterRequest;

public interface AuthService {

    /**
     * Аутентификация пользователя (вход по email и паролю)
     *
     * @param authRequest логин и пароль
     * @return JwtResponseDto с access и refresh токенами
     */
    JwtResponseDto authenticateUser(AuthRequest authRequest);

    /**
     * Регистрация нового пользователя
     *
     * @param request данные нового пользователя
     * @return сохранённый пользователь
     */
    User registerUser(RegisterRequest request);

    /**
     * Обновление токенов по refresh-токену
     *
     * @param request refresh-токен
     * @return новый access и refresh токен
     */
    JwtResponseDto refreshTokens(RefreshTokenRequest request);

    /**
     * Удаление всех refresh-токенов пользователя
     *
     * @param id ID пользователя
     */
    void deleteAllById(Long id);
}
