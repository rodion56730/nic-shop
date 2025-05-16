package org.nicetu.nicshop.service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

@Service
public class CookieService {
    private final HttpServletResponse httpServletResponse;
    private final HttpServletRequest httpServletRequest;

    @Autowired
    public CookieService(HttpServletResponse httpServletResponse, HttpServletRequest httpServletRequest){
        this.httpServletResponse = httpServletResponse;
        this.httpServletRequest = httpServletRequest;
    }

    public void setCookie(String name, String value) {
        int expiresInSeconds = (6 * 60 * 60);
        Cookie cookie = new Cookie(name, URLEncoder.encode(value, StandardCharsets.UTF_8));
        cookie.setSecure(true);
        cookie.setHttpOnly(true);
        cookie.setDomain("localhost");
        cookie.setPath("/");
        cookie.setMaxAge(expiresInSeconds);
        httpServletResponse.addCookie(cookie);
    }

    public void deleteCookie(String name) {
        Cookie cookie = new Cookie(name, null);
        cookie.setSecure(true);
        cookie.setHttpOnly(true);
        cookie.setDomain("localhost");
        cookie.setPath("/");
        cookie.setMaxAge(0);
        httpServletResponse.addCookie(cookie);
    }

    public Cookie[] getAllCookies() {
        return httpServletRequest.getCookies();
    }

    public Cookie getCookie(String name) {
        if (httpServletRequest.getCookies() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Товар в корзине не найден");
        }

        List<Cookie> cookies = Arrays.stream(httpServletRequest.getCookies()).filter(c -> c.getName().equals(name)).toList();

        if (cookies.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Товар в корзине не найден");
        }

        return cookies.get(0);
    }
}
