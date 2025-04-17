package com.ourgroup.railway.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")  // 应用于所有路径
                .allowedOrigins("http://localhost:5173")  // 允许的前端源（Vite默认端口）
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")  // 允许的HTTP方法
                .allowedHeaders("*")  // 允许所有请求头
                .exposedHeaders("Authorization")  // 暴露的响应头（如果需要）
                .allowCredentials(true)  // 允许携带凭证（cookies等）
                .maxAge(3600);  // 预检请求的缓存时间（秒）
    }
}