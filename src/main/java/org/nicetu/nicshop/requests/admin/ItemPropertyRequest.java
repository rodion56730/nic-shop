package org.nicetu.nicshop.requests.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.nicetu.nicshop.utils.validation.Marker;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ItemPropertyRequest {
    @Null(groups = Marker.onCreate.class)
    @NotNull(groups = Marker.onUpdate.class)
    @Schema(description = "Идентификатор свойств продукта. Указывается при редактировании", required = true)
    private Long id;

    @NotNull
    @Schema(description = "Идентификаторы конкретных свойств продукта", required = true)
    private List<Long> propertyIds;
}
