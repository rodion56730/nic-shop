package org.nicetu.nicshop.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class AddItemRequest {
    @Schema(description = "Id товара, который надо добавить")
    @NotNull
    private Long productId;
}