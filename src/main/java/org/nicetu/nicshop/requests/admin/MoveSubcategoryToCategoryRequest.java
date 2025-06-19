package org.nicetu.nicshop.requests.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

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
