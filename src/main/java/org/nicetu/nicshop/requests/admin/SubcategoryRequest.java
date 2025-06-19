package org.nicetu.nicshop.requests.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;
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
