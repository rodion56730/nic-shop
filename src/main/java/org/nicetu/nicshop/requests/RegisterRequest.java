package org.nicetu.nicshop.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RegisterRequest {
    @Schema(description = "Имя пользователя", required = true)
    @NotBlank
    private String firstName;

    @Schema(description = "Фамилия пользователя", required = true)
    @NotBlank
    private String lastName;

    @Schema(description = "Mail пользователя", required = true)
    @NotBlank
    @Email(regexp = "[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,3}",
            flags = Pattern.Flag.CASE_INSENSITIVE)
    private String userEmail;

    @Schema(description = "Пароль пользователя", required = true)
    @NotBlank
    private String userPassword;
}
