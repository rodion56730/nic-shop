package org.nicetu.nicshop.requests.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MoveSubcategoryToCategoryRequest {
    @NotNull
    @Schema(description = "Идентификатор подкатегории, которую нужно переместить", required = true)
    private Long subId;

    @NotNull
    @Schema(description = "Идентификатор категории, в которую нужно переместить", required = true)
    private Long catId;
}
