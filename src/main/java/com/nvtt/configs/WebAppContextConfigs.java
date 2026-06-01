/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvtt.configs;

import com.nvtt.formatters.RoleFormatter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 *
 * @author vthan
 */
@Configuration
@ComponentScan(
        basePackages = {
            "com.nvtt.controllers",
            "com.nvtt.services",
            "com.nvtt.repositories",
            "com.nvtt.utils",
            "com.nvtt.scheduler"
        }
)
@EnableWebMvc
@EnableTransactionManagement
public class WebAppContextConfigs implements WebMvcConfigurer {
       
    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addFormatter(new RoleFormatter());
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/img/**").addResourceLocations("classpath:/static/images");
        registry.addResourceHandler("/css/**").addResourceLocations("classpath:/static/css");
        registry.addResourceHandler("/js/**").addResourceLocations("classpath:/static/js");
    }

    @Bean
    public StandardServletMultipartResolver multipartResolver() {
        return new StandardServletMultipartResolver();
    }

    //interceptor
    @Bean
    HandlerInterceptor getPermissionInterceptor() {
        return new PermissionInterceptorConfig();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        String[] whiteList = {
            "/admin/login", "/admin/register" , "/admin/error/**", "/api/v1/public/**"
        };
        registry.addInterceptor(getPermissionInterceptor())
                .excludePathPatterns(whiteList);
    }

}
