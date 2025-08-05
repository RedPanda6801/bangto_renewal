package com.example.banto.Utils;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.UUID;

public class CookieHandler {
    private static final String CART_ID_COOKIE = "cart_id";
    public String cookieHandler(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (CART_ID_COOKIE.equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }

        // 없다면 새로 생성
        String uuid = UUID.randomUUID().toString();
        Cookie cookie = new Cookie(CART_ID_COOKIE, uuid);
        cookie.setPath("/"); // 전체 경로에 적용
        cookie.setMaxAge(60 * 60 * 24 * 7); // 7일 유지
        response.addCookie(cookie);
        return uuid;
    }
}