package org.nicetu.nicshop.security.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.nicetu.nicshop.domain.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.sql.Date;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Slf4j
@Component
public class JwtProvider {
    private final SecretKey jwtAccessSecret;
    private final SecretKey jwtRefreshSecret;
    private final int jwtAccessDurationSec;
    private final int jwtRefreshDurationDays;

    public JwtProvider(
            @Value("${shop.jwt.secret.access}") String jwtAccessSecret,
            @Value("${shop.jwt.secret.refresh}") String jwtRefreshSecret,
            @Value("${shop.jwt.expires.access-sec}") int accessDuration,
            @Value("${shop.jwt.expires.refresh-days}") int refreshDuration
    ) {
        this.jwtAccessSecret = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtAccessSecret));
        this.jwtRefreshSecret = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtRefreshSecret));
        this.jwtAccessDurationSec = accessDuration;
        this.jwtRefreshDurationDays = refreshDuration;
    }

    public TokenResponse generateAccessToken(User user) {
        Instant expiration = Instant.now().plus(jwtAccessDurationSec, ChronoUnit.SECONDS);
        String token = Jwts.builder()
                .subject(String.valueOf(user.getId()))
                .issuedAt(Date.from(expiration))
                .signWith(jwtAccessSecret)
                .claim("roles", user.getRoles())
                .compact();
        return new TokenResponse(token, expiration);
    }

    public TokenResponse generateRefreshToken(User user) {
        Instant expiration = Instant.now().plus(jwtRefreshDurationDays, ChronoUnit.DAYS);
        String token = Jwts.builder()
                .subject(String.valueOf(user.getId()))
                .issuedAt(Date.from(expiration))
                .signWith(jwtRefreshSecret)
                .compact();
        return new TokenResponse(token, expiration);
    }

    public boolean validateAccessToken(String accessToken) {
        return validateToken(accessToken, jwtAccessSecret);
    }

    public boolean validateRefreshToken(String refreshToken) {
        return validateToken(refreshToken, jwtRefreshSecret);
    }

    private boolean validateToken(String token, Key secret) {
        try {
            Jwts.parser()
                    .verifyWith((SecretKey) secret)
                    .build()
                    .parseSignedClaims(token);
            return true;

        } catch (SignatureException e) {
            log.error("Invalid token signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            log.error("Invalid token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            log.error("Token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.error("Token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("Token claims string is empty: {}", e.getMessage());
        }

        return false;
    }

    public Claims getAccessClaims(String accessToken) {
        return getClaims(accessToken, jwtAccessSecret);
    }

    public Claims getRefreshClaims(String refreshToken) {
        return getClaims(refreshToken, jwtRefreshSecret);
    }

    private Claims getClaims(String token, Key secret) {
        return Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(secret.getEncoded()))
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
