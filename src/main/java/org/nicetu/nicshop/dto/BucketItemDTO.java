package org.nicetu.nicshop.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BucketItemDTO {
    @Schema(description = "Ссылка на картинку для товара", required = true)
    private String pictureUrl;

    @Schema(description = "Название товара", required = true)
    private String name;

    @Schema(description = "Цена на товар", required = true)
    private Long price;

    @Schema(description = "Количество товара в магазине", required = true)
    private Long amount;
}
