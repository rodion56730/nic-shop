package org.nicetu.nicshop.requests.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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
