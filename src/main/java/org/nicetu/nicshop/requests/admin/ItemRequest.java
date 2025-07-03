package org.nicetu.nicshop.requests.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;
import org.nicetu.nicshop.utils.validation.Marker;

@Getter
@Setter
@NoArgsConstructor
public class ItemRequest {

    @Null(groups = Marker.onCreate.class)
    @NotNull(groups = Marker.onUpdate.class)
    @Schema(description = "Идентификатор продукта. Указывается при редактировании", required = true)
    private Long id;

    @NotNull
    @Schema(description = "ID подкатегории, в которую входит данный продукт", required = true)
    private Long subcategoryId;

    @NotBlank
    @Size(max = 255)
    @Schema(description = "Название продукта", required = true)
    private String name;

    @NotBlank
    @Size(max = 500)
    @Schema(description = "Описание продукта", required = true)
    private String description;

    @NotNull
    @Schema(description = "Количество продукта", required = true)
    private Long amount;

    @NotBlank
    @Schema(description = "Ссылка на фотографию", required = true)
    private String pictureUrl;

    @NotNull
    @Schema(description = "Цена товара", required = true)
    private Long price;

    @NotNull
    @Schema(description = "Цена товара по скидке (null, если скидки нет)")
    private Long discountPrice;

    @NotNull
    @Schema(description = "Наличие скидки", required = true)
    private Boolean discount;

    @NotNull
    @Schema(description = "Свойства товара", required = true)
    private @Valid ItemPropertyRequest itemPropertyRequest;

}
