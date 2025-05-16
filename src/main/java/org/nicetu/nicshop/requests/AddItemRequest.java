package org.nicetu.nicshop.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddItemRequest {
    @Schema(description = "Id товара, который надо добавить")
    @NotNull
    private Long productId;
}