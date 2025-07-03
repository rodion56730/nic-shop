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
import org.nicetu.nicshop.service.api.BucketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import javax.validation.Valid;

@Tag(name = "Корзина", description = "Операции с товарами")
@Slf4j
@RestController
@RequestMapping("/api/cart")
public class BucketController {
    private final BucketService bucketService;

    @Autowired
    public BucketController(BucketService bucketService) {
        this.bucketService = bucketService;
    }

    @Operation(summary = "Получить товары, добавленные в корзину")
    @GetMapping()
    public ResponseEntity<BucketDTO> getCartProducts(JwtAuthentication authentication) {
        return ResponseEntity.ok(bucketService.getUserProducts(authentication));
    }

    @Operation(summary = "Добавить товар в корзину")
    @ApiResponses({
            @ApiResponse(responseCode = "404", description = "Товар не существует"),
            @ApiResponse(responseCode = "400", description = "Товара нет в наличии")
    })
    @PostMapping("/product")
    public void addProduct(
            @Valid @RequestBody AddItemRequest request,
            JwtAuthentication authentication
    ) {
        bucketService.addProduct(request, authentication);
    }

    @Operation(description = "Увеличить количество товара в корзине")
    @PutMapping("/amount")
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
    @PostMapping("/transaction")
    @ApiResponses({
            @ApiResponse(responseCode = "500", description = "Ошибка на сервере почты"),
            @ApiResponse(responseCode = "404", description = "Товар не существует"),
            @ApiResponse(responseCode = "400", description = "Корзина пустая")
    })
    public void buyProducts(JwtAuthentication authentication) {
        bucketService.buyProducts(authentication);
    }

    @Operation(description = "Удалить продукт из корзины")
    @DeleteMapping("/product")
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
