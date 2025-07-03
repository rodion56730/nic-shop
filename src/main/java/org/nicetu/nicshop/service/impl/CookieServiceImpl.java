package org.nicetu.nicshop.service.impl;

import org.nicetu.nicshop.service.api.CookieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CookieServiceImpl implements CookieService {
    private final HttpServletResponse httpServletResponse;
    private final HttpServletRequest httpServletRequest;

    @Autowired
    public CookieServiceImpl(HttpServletResponse httpServletResponse, HttpServletRequest httpServletRequest){
        this.httpServletResponse = httpServletResponse;
        this.httpServletRequest = httpServletRequest;
    }

    public void setCookie(String name, String value) {
        int expiresInSeconds = (6 * 60 * 60);
        try {
            Cookie cookie = new Cookie(name, URLEncoder.encode(value, "UTF-8"));
            cookie.setPath("/");
            cookie.setHttpOnly(true);
            cookie.setSecure(true);
            cookie.setHttpOnly(true);
            cookie.setDomain("localhost");
            cookie.setPath("/");
            cookie.setMaxAge(expiresInSeconds);
            httpServletResponse.addCookie(cookie);        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("UTF-8 encoding not supported", e);
        }

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

        List<Cookie> cookies = Arrays.stream(httpServletRequest.getCookies())
                .filter(c -> c.getName().equals(name))
                .collect(Collectors.toList());
        if (cookies.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Товар в корзине не найден");
        }

        return cookies.get(0);
    }

}
