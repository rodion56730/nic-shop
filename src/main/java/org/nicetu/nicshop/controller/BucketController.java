package org.nicetu.nicshop.controller;



import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.nicetu.nicshop.dto.BucketDTO;
import org.nicetu.nicshop.requests.AddItemRequest;
import org.nicetu.nicshop.requests.CartItemRequest;
import org.nicetu.nicshop.security.jwt.JwtAuthentication;
import org.nicetu.nicshop.service.BucketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Tag(name = "Корзина", description = "Операции с товарами")
@Slf4j
@RestController
@RequestMapping("/api")
public class BucketController {
    private final BucketService bucketService;

    @Autowired
    public BucketController(BucketService bucketService) {
        this.bucketService = bucketService;
    }

    @Operation(summary = "Получить товары, добавленные в корзину")
    @GetMapping("/cart")
    public ResponseEntity<BucketDTO> getCartProducts(JwtAuthentication authentication) {
        return ResponseEntity.ok(bucketService.getUserProducts(authentication));
    }

    @Operation(summary = "Добавить товар в корзину")
    @ApiResponses({
            @ApiResponse(responseCode = "404", description = "Товар не существует"),
            @ApiResponse(responseCode = "400", description = "Товара нет в наличии")
    })
    @PostMapping("/addProduct")
    public void addProduct(
            @Valid @RequestBody AddItemRequest request,
            JwtAuthentication authentication
    ) {
        bucketService.addProduct(request, authentication);
    }

    @Operation(description = "Увеличить количество товара в корзине")
    @PutMapping("/cart/addAmount")
    @ApiResponses({
            @ApiResponse(responseCode = "404", description = "Товар не существует"),
            @ApiResponse(responseCode = "400", description = "Превышен лимит товара \n Товар не добавлен в корзину")
    })
    public void addAmount(@Valid @RequestBody CartItemRequest request, JwtAuthentication authentication) {
        bucketService.addAmount(request, authentication);
    }

    @Operation(description = "Уменьшить количество товара в корзине")
    @PostMapping("/cart/reduceAmount")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = "Товар не существует"),
            @ApiResponse(responseCode = "400", description = "Товар не добавлен в корзину")
    })
    public void reduceAmount(@Valid @RequestBody CartItemRequest request, JwtAuthentication authentication) {
        bucketService.reduceAmount(request, authentication);
    }

    @Operation(description = "Совершить покупку")
    @PostMapping("/cart/transaction")
    @ApiResponses({
            @ApiResponse(responseCode = "500", description = "Ошибка на сервере почты"),
            @ApiResponse(responseCode = "404", description = "Товар не существует"),
            @ApiResponse(responseCode = "400", description = "Корзина пустая")
    })
    public void buyProducts(JwtAuthentication authentication) {
        bucketService.buyProducts(authentication);
    }

    @Operation(description = "Удалить продукт из корзины")
    @DeleteMapping("/cart/deleteProduct")
    @ApiResponses({
            @ApiResponse(responseCode = "404", description = "Товар не существует"),
            @ApiResponse(responseCode = "400", description = "Товар не добавлен в корзину")
    })
    public void deleteProduct(
            @Valid @RequestBody CartItemRequest request,
            JwtAuthentication authentication
    ) {
        bucketService.deleteProduct(request, authentication);
    }
}
