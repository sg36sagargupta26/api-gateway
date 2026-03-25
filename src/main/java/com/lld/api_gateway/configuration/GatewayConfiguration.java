package com.lld.api_gateway.configuration;

import com.lld.api_gateway.rateLimiter.service.RateLimitService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;

import java.net.URI;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.springframework.cloud.gateway.server.mvc.common.MvcUtils.setRequestUrl;
import static org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions.route;
import static org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions.http;

@Configuration(proxyBeanMethods = false)
public class GatewayConfiguration {
    List<URI> servers = List.of(
            URI.create("http://localhost:8080"),
            URI.create("http://localhost:9090")
    );
    AtomicInteger counter = new AtomicInteger(0);
    @Bean
    public RouterFunction<ServerResponse> helloProxyRoute(RateLimitService rateLimitService) {
        return route("hello_proxy_route")
                .GET("/hello", http())
                .before(req -> {
                    int index = Math.abs(counter.getAndIncrement() % servers.size());
                    URI selectedUri = servers.get(index);
                    setRequestUrl(req, selectedUri);
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
