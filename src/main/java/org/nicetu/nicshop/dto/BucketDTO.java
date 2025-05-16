package org.nicetu.nicshop.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BucketDTO {
    @Schema(description = "Список товаров в корзине")
    private List<BucketItemDTO> items;

    @Schema(description = "Количество товаров в корзине")
    private Long count;

    @Schema(description = "Общая цена товаров в корзине")
    private Long fullPrice;
}
