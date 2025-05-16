package org.nicetu.nicshop.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartItemRequest {
    @Schema(description = "Id товара в корзине")
    @NotNull
    private Long productId;
}