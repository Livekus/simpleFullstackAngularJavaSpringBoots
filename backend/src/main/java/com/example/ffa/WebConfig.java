package com.example.ffa;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.*;
import org.springframework.web.filter.ShallowEtagHeaderFilter;

@Configuration
public class WebConfig {
    @Bean
    FilterRegistrationBean<ShallowEtagHeaderFilter> etagFilter() {
        var frb = new FilterRegistrationBean<>(new ShallowEtagHeaderFilter());
        frb.addUrlPatterns("/api/*");
        frb.setName("etagFilter");
        return frb;
    }
}