package org.nicetu.nicshop.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.nicetu.nicshop.security.jwt.TokenResponse;
import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
public class JwtResponseDto {

    @Schema(description = "Тип токена")
    private final String tokenType = "Bearer";

    @Schema(description = "Токен доступа")
    private String accessToken;

    @Schema(description = "Когда токен истекает")
    private Instant accessExpiresIn;

    @Schema(description = "Refresh токен")
    private String refreshToken;

    @Schema(description = "Когда Refresh токен истекает")
    private Instant refreshExpiresIn;

    public JwtResponseDto(TokenResponse access, TokenResponse refresh) {
        this.accessToken = access.token;
        this.accessExpiresIn = access.expiresIn;
        this.refreshToken = refresh.token;
        this.refreshExpiresIn = refresh.expiresIn;
    }

}
