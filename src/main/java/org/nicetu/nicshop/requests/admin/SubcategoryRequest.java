package org.nicetu.nicshop.requests.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.nicetu.nicshop.utils.validation.Marker;

@Getter
@Setter
@NoArgsConstructor
public class SubcategoryRequest {
    @Null(groups = Marker.onCreate.class)
    @NotNull(groups = Marker.onUpdate.class)
    @Schema(description = "Идентификатор подкатегории. Указывается при редактировании", required = true)
    private Long id;

    @NotNull
    @Schema(description = "ID категории, в которую входит данная подкатегория", required = true)
    private Long categoryId;

    @Schema(description = "ID родительской подкатегории (null, если родителя нет)")
    private Long parentId;

    @NotBlank
    @Size(max = 255)
    @Schema(description = "Название подкатегории", required = true)
    private String name;
}
