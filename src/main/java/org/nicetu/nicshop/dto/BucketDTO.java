package org.nicetu.nicshop.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Builder;
import lombok.AllArgsConstructor;

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
