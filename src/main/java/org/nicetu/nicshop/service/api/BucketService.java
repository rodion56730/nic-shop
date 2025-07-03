package org.nicetu.nicshop.service.api;

import org.nicetu.nicshop.dto.BucketDTO;
import org.nicetu.nicshop.requests.AddItemRequest;
import org.nicetu.nicshop.requests.CartItemRequest;
import org.nicetu.nicshop.security.jwt.JwtAuthentication;

public interface BucketService {

    /**
     * Получить содержимое корзины текущего пользователя или из cookies
     */
    BucketDTO getUserProducts(JwtAuthentication authentication);

    /**
     * Уменьшить количество конкретного товара в корзине
     */
    void reduceAmount(CartItemRequest request, JwtAuthentication authentication);

    /**
     * Увеличить количество конкретного товара в корзине
     */
    void addAmount(CartItemRequest request, JwtAuthentication authentication);

    /**
     * Добавить товар в корзину
     */
    void addProduct(AddItemRequest request, JwtAuthentication authentication);

    /**
     * Удалить товар из корзины
     */
    void deleteProduct(CartItemRequest request, JwtAuthentication authentication);

    /**
     * Купить все товары из корзины
     */
    void buyProducts(JwtAuthentication authentication);
}
