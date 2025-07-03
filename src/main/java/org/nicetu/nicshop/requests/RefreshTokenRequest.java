package org.nicetu.nicshop.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
public class RefreshTokenRequest {

    @Schema(description = "Refresh токен", required = true)
    @NotBlank
    private String refreshToken;

}
