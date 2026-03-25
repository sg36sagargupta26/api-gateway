package com.lld.api_gateway.configuration;

import com.lld.api_gateway.rateLimiter.service.RateLimitService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

import static org.springframework.cloud.gateway.server.mvc.filter.BeforeFilterFunctions.uri;
import static org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions.route;
import static org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions.http;

@Configuration(proxyBeanMethods = false)
public class GatewayConfiguration {
    @Bean
    public RouterFunction<ServerResponse> helloProxyRoute(RateLimitService rateLimitService) {
        return route("hello_proxy_route")
                .GET("/hello", http())
                .filter((req,next)->{
                    String userId = req.headers().firstHeader("user-id");
                    if (userId != null && rateLimitService.isNotAllowed(userId)){
                        return ServerResponse.status(HttpStatus.TOO_MANY_REQUESTS).build();
                    }
                    return next.handle(req);
                })
                .before(uri("http://localhost:8080"))
                .build();
    }

}
