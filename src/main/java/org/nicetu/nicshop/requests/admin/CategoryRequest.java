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
