package org.nicetu.nicshop.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

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
