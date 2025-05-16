package org.nicetu.nicshop.requests.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
public class PropertyRequest {
    @NotNull
    @Schema(description = "Идентификатор свойства", required = true)
    private Long id;

    @NotBlank
    @Schema(description = "Название свойства", required = true)
    private String name;

    @NotBlank
    @Schema(description= "Значение свойства", required = true)
    private String value;
}
