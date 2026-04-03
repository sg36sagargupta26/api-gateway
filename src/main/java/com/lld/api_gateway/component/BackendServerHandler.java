package com.lld.api_gateway.component;

import com.lld.api_gateway.service.BackendServerService;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;

import java.net.URI;
import java.util.List;
import java.util.Map;

@Component
public class BackendServerHandler {

    private final BackendServerService backendServerService;

    public BackendServerHandler(final BackendServerService backendServerService) {
        this.backendServerService = backendServerService;
    }


    public ServerResponse listServers(ServerRequest request) {
        List<String> servers = backendServerService.getAllServers()
                .stream()
                .map(URI::toString)
                .toList();

        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(servers);
    }

    public ServerResponse addServer(ServerRequest request) throws Exception {
        Map<String, String> body = request.body(Map.class);
        String uri = body.get("uri");

        if (uri == null || uri.isBlank()) {
            return ServerResponse.badRequest().body("Missing 'uri'");
        }

        boolean added = backendServerService.addServer(uri);
        if (!added) {
            return ServerResponse.status(409).body("URI already exists");
        }

        return ServerResponse.ok().body("Server added");
    }

    public ServerResponse removeServer(ServerRequest request) throws Exception {
        Map<String, String> body = request.body(Map.class);
        String uri = body.get("uri");

        if (uri == null || uri.isBlank()) {
            return ServerResponse.badRequest().body("Missing 'uri'");
        }

        boolean removed = backendServerService.removeServer(uri);
        if (!removed) {
            return ServerResponse.status(404).body("URI not found");
        }

        return ServerResponse.ok().body("Server removed");
    }

}