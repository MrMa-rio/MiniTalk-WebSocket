package com.marsn.minitalkwebsocket.adapter.configuration;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    static String packagePath = "com.marsn.minitalkwebsocket.entrypoint.";

    @Bean
    public GroupedOpenApi messageApi() {
        return GroupedOpenApi.builder()
                .group("WebSocket")
                .packagesToScan(packagePath + "websocket")
                .build();
    }
}
