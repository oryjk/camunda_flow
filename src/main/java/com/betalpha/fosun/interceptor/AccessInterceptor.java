package com.betalpha.fosun.interceptor;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AccessInterceptor extends HandlerInterceptorAdapter {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        return access(request);
    }

    private boolean access(HttpServletRequest request) {
        String sessionIdStr = "";
        Cookie[] cookies = request.getCookies();
        if (null != cookies) {
            for (Cookie cookie : cookies) {
                if ("cookie_user".equals(cookie.getName())) {
                    sessionIdStr = cookie.getValue();
                }
            }
        }
        return !"".equals(sessionIdStr);
    }
}
