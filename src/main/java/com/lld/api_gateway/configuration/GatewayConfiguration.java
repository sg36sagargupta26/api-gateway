package com.lld.api_gateway.configuration;

import com.lld.api_gateway.rateLimiter.service.RateLimitService;
import com.lld.api_gateway.service.BackendServerService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

import java.util.concurrent.*;

import static org.springframework.cloud.gateway.server.mvc.common.MvcUtils.setRequestUrl;
import static org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions.route;
import static org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions.http;

@Configuration(proxyBeanMethods = false)
public class GatewayConfiguration {

    @Bean
    public RouterFunction<ServerResponse> helloProxyRoute(final RateLimitService rateLimitService, final BackendServerService backendServerService) {
        return route("hello_proxy_route")
                .GET("/hello", http())
                .before(req -> {
                    setRequestUrl(req, backendServerService.nextServer());
                    return req;
                })
                .filter((req,next)->{
                    String userId = req.headers().firstHeader("user-id");
                    if (userId != null && rateLimitService.isNotAllowed(userId)){
                        return ServerResponse.status(HttpStatus.TOO_MANY_REQUESTS).build();
                    }
                    return next.handle(req);
                })
                .build();
    }
}
