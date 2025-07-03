package org.nicetu.nicshop.security.jwt;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
public class TokenResponse {

    public String token;

    public Instant expiresIn;

}
