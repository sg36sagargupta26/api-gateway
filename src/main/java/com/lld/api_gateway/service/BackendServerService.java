package com.lld.api_gateway.service;

import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class BackendServerService {
    private final CopyOnWriteArrayList<URI> servers = new CopyOnWriteArrayList<>(
            List.of(
                    URI.create("http://localhost:8080"),
                    URI.create("http://localhost:9090")
            )
    );
    private final AtomicInteger counter = new AtomicInteger(0);

    public URI nextServer() {
        if (servers.isEmpty()) {
            throw new IllegalStateException("No backend servers configured");
        }
        int index = Math.floorMod(counter.getAndIncrement(), servers.size());
        return servers.get(index);
    }

    public List<URI> getAllServers() {
        return List.copyOf(servers);
    }

    public boolean addServer(String uri) {
        URI newUri = URI.create(uri);
        if (servers.contains(newUri)) {
            return false;
        }
        servers.add(newUri);
        return true;
    }

    public boolean removeServer(String uri) {
        return servers.remove(URI.create(uri));
    }
}
