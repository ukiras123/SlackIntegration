package com.kiran.controller.intercept;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;
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
        // For CORS preflighted we should be expecting to receive 2 requests:
        // 1) Browsers generated OPTIONS request for authorization with headers "origin" and "access-control-request-headers".
        // "access-control-request-headers" should have both "x-ocx-tenant" and "x-ocx-username" inside.
        // As on the next call we are expecting for them.
        // 2) Normal request with "x-ocx-tenant" and "x-ocx-username" plus "origin"
        if (!StringUtils.isBlank(request.getHeader("origin"))
                && !StringUtils.isBlank(request.getHeader("access-control-request-headers"))
          ) {
            if (!request.getMethod().equals(RequestMethod.OPTIONS.name())) {
                //someone tries to simulate CORS request. Not OPTIONS was sent.
                String errorResponseString = "Invalid CORS request";
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().write(errorResponseString);
                return false;
            } else {
                return true;
            }
        }


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

