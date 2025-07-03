package org.nicetu.nicshop.service.api;

import javax.servlet.http.Cookie;

public interface CookieService {
    void setCookie(String name, String value);

    void deleteCookie(String name);

    Cookie[] getAllCookies();

    Cookie getCookie(String name);
}
