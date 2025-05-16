package org.nicetu.nicshop.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class CatalogDTO {
    @Schema(description = "Инициалы пользователя (null, если пользователь не авторизован)")
    private String initials;

    @Schema(description = "Список товаров", required = true)
    private List<ItemDTO> productDtos;

    @Schema(description = "Количество товаров в данной категории/подкатегории", required = true)
    private Long categoryCount;

    @Schema(description = "Количество товаров, добавленных в корзину", required = true)
    private Long cartCount;
}