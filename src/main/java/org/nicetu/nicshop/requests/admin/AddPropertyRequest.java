package org.nicetu.nicshop.requests.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import org.nicetu.nicshop.utils.validation.Marker;

@Getter
@Setter
@NoArgsConstructor
public class AddPropertyRequest {
    @Null(groups = Marker.onCreate.class)
    @NotNull(groups = Marker.onUpdate.class)
    @Schema(description = "Идентификатор свойства", required = true)
    private Long id;

    @NotNull
    @Schema(description = "Идентификатор набора свойств для продукта", required = true)
    private Long prodPropId;

    @NotBlank
    @Schema(description = "Название свойства", required = true)
    private String name;

    @NotBlank
    @Schema(description = "Значение свойства", required = true)
    private String value;
}
