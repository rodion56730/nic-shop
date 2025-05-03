package org.nicetu.nicshop.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import jakarta.validation.constraints.NotNull;

@Getter
@Setter
public class AddProductRequest {
    @Schema(description = "Id товара, который надо добавить", required = true)
    @NotNull
    private Long productId;
}