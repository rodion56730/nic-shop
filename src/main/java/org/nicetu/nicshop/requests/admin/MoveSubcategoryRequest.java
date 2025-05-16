package org.nicetu.nicshop.requests.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MoveSubcategoryRequest {
    @NotNull
    @Schema(description = "Идентификатор подкатегории, которую нужно переместить", required = true)
    private Long id;

    @NotNull
    @Schema(description = "Идентификатор родительской подкатегории, в которую нужно переместить", required = true)
    private Long destId;
}
