package com.kiran.controller.intercept;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * @author Kiran
 * @since 9/9/17
 */
@Configuration
public class RequestInterceptorConfig extends WebMvcConfigurerAdapter {

    @Autowired
    @Qualifier("securityHeaderInterceptor")
    HandlerInterceptor securityHeaderInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(securityHeaderInterceptor);
    }

}