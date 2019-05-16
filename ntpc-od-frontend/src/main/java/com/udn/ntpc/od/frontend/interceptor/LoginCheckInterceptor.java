package com.udn.ntpc.od.frontend.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import static com.udn.ntpc.od.frontend.controller.PageController.USER_KEY;

@Slf4j
public class LoginCheckInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("{}", request.getRequestURI());

        HttpSession session = request.getSession();
        String userAccount = (String) session.getAttribute(USER_KEY);
        if (!request.getRequestURI().endsWith("login.html") && StringUtils.isBlank(userAccount)) {
            response.sendRedirect("/login.html");
            return false;
        } else {
            return super.preHandle(request, response, handler);
        }
    }
}
