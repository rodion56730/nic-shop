package org.nicetu.nicshop.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import jakarta.validation.constraints.NotNull;

@Getter
@Setter
public class CartProductRequest {
    @Schema(description = "Id товара в корзине")
    @NotNull
    private Long productId;
}