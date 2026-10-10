package com.neusoft.yiyang.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 跨域配置。
 *
 * <p>前端开发时用的是 Vite 代理（/api → http://localhost:8080），其实同源、不会跨域；
 * 但如果你直接用浏览器或者 Postman 访问后端，或者把前端打包后放到别的端口，
 * 就需要这份配置。</p>
 *
 * <p>说明：本模块不涉及登录鉴权，JWT 校验由「用户管理」模块统一提供，
 * 需要时可以在这里注册一个 HandlerInterceptor，拦截除 /auth/** 之外的请求。</p>
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600);
    }
}
