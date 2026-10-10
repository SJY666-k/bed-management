package com.neusoft.yiyang.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Swagger（springdoc-openapi）配置。
 *
 * <p>启动后访问：<a href="http://localhost:8080/api/swagger-ui.html">http://localhost:8080/api/swagger-ui.html</a></p>
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI bedManagementOpenApi() {
        return new OpenAPI().info(new Info()
                .title("东软颐养中心管理系统 —— 床位管理模块接口文档")
                .description("包含「床位示意图」与「床位管理」两大功能的接口，"
                        + "统一返回体为 {code, msg, data}，code=200 表示业务成功。")
                .version("1.0.0")
                .contact(new Contact().name("床位管理模块")));
    }
}
