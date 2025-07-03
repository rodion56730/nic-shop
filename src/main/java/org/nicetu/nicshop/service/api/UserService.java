package org.nicetu.nicshop.service.api;

import org.nicetu.nicshop.domain.User;
import org.nicetu.nicshop.dto.UserDTO;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

    /**
     * Регистрация пользователя на основе DTO
     *
     * @param userDTO DTO с данными пользователя
     * @return true, если пользователь сохранён
     */
    boolean save(UserDTO userDTO);

    /**
     * Создание пользователя (с проверками на уникальность)
     *
     * @param user объект пользователя
     * @return сохранённый пользователь
     */
    User create(User user);

    /**
     * Получение пользователя по username
     *
     * @param username имя пользователя
     * @return пользователь
     */
    User getByUsername(String username);

    /**
     * Получение текущего авторизованного пользователя
     *
     * @return текущий пользователь
     */
    User getCurrentUser();

    /**
     * Получение пользователя по ID
     *
     * @param id идентификатор пользователя
     * @return пользователь
     */
    User getUserById(Long id);

    /**
     * Назначение пользователю роли ADMIN (deprecated)
     */
    @Deprecated
    void setAdmin();
}
