package com.lld.api_gateway.configuration;

import com.lld.api_gateway.component.BackendServerHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

import static org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions.route;

@Configuration
public class AdminConfiguration {
    @Bean
    RouterFunction<ServerResponse> backendServerAdminRoutes(BackendServerHandler handler) {
        return route()
                .GET("/admin/servers", handler::listServers)
                .POST("/admin/servers", handler::addServer)
                .DELETE("/admin/servers", handler::removeServer)
                .build();
    }
}
