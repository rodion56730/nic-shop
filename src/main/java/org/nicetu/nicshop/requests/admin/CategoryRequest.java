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
public class CategoryRequest {
    @Null(groups = Marker.onCreate.class)
    @NotNull(groups = Marker.onUpdate.class)
    @Schema(description = "Идентификатор категории. Указывается при редактировании", required = true)
    private Long id;

    @NotBlank
    @Size(max = 255)
    @Schema(description = "Название категории", required = true)
    private String name;

    @Schema(description = "Идентификатор категории родителя. Указывается при редактировании", required = true)
    private Long parentId;
}
