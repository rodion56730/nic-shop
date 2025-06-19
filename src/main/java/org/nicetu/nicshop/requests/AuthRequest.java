package org.nicetu.nicshop.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
public class AuthRequest {
    @Schema(description = "Mail пользователя")
    @NotBlank
    private String userEmail;

    @Schema(description = "Пароль пользователя")
    @NotBlank
    private String userPassword;
}
