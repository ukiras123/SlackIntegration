package com.kiran.controller.intercept;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * @author Kiran
 * @since 9/9/17
 */


@Component("securityHeaderInterceptor")
public class HeaderRequestInterceptor implements HandlerInterceptor {

    public HeaderRequestInterceptor() {
    }

    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception exception) throws Exception {
    }

    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
    }

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String accept = request.getHeader("Accept");

        if (!accept.contains("*/*") && !StringUtils.isBlank(accept)) {
            if (!accept.equalsIgnoreCase("application/json")) {
                String acceptError = "{\"error\" : \"Only application/json accpeted in header.\"}";
                response.setStatus(415);
                response.getWriter().write(acceptError);
                return false;
            }
        }
        return true;
    }
}

