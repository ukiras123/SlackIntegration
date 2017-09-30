package com.kiran.controller.intercept;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
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

    @Value("${endpoints.cors.allowed-origins}")
    private String[] allowedOrigins;

    @Value("${endpoints.cors.allowed-methods}")
    private String[] allowedMethods;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(securityHeaderInterceptor);
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedOrigins(allowedOrigins).allowedMethods(allowedMethods);
    }

}