package org.nicetu.nicshop.security.jwt;

import io.jsonwebtoken.Claims;
import org.nicetu.nicshop.domain.Role;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class JwtUtils {
    public static JwtAuthentication getAuthentication(Claims claims) {
        final JwtAuthentication authentication = new JwtAuthentication();
        authentication.setUserId(Long.valueOf(claims.getSubject()));
        authentication.setRoles(getRoles(claims));
        return authentication;
    }

    private static Set<Role> getRoles(Claims claims) {
        List<?> roles = claims.get("roles", List.class);
        return roles.stream().map(role -> Role.valueOf((String) role)).collect(Collectors.toSet());
    }

}

