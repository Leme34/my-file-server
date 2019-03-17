package com.lee.myfileserver.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * 为了支持跨域请求，我们设置了安全配置类 SecurityConfig：
 */
@Configuration
public class SecurityConfig extends WebMvcConfigurerAdapter{

    //配合注解@CrossOrigin工作
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedOrigins("*"); // 允许跨域请求
    }

}
