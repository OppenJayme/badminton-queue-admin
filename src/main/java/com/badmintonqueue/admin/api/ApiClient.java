package com.badmintonqueue.admin.api;

import com.badmintonqueue.admin.config.Config;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Optional;

public class ApiClient {
    private final HttpClient client;
    private final ObjectMapper mapper = new ObjectMapper();
    private final String baseUrl;
    private String token;

    public ApiClient(Config config) {
        this.baseUrl = config.getApiBaseUrl();
        this.client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(5))
                .build();
    }

    public boolean login(String email, String password) throws IOException, InterruptedException {
        ObjectNode payload = mapper.createObjectNode()
                .put("email", email)
                .put("password", password);
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/api/auth/login"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(mapper.writeValueAsString(payload)))
                .build();
        HttpResponse<String> res = client.send(req, HttpResponse.BodyHandlers.ofString());
        if (res.statusCode() >= 200 && res.statusCode() < 300) {
            ObjectNode node = (ObjectNode) mapper.readTree(res.body());
            this.token = Optional.ofNullable(node.get("token")).map(v -> v.asText()).orElse(null);
            return token != null;
        }
        return false;
    }

    public Optional<TotalsDto> getTotals() {
        try {
            HttpRequest req = HttpRequest.newBuilder()
                    .uri(URI.create(baseUrl + "/api/admin/totals"))
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + token)
                    .GET()
                    .build();
            HttpResponse<String> res = client.send(req, HttpResponse.BodyHandlers.ofString());
            if (res.statusCode() >= 200 && res.statusCode() < 300) {
                return Optional.of(mapper.readValue(res.body(), TotalsDto.class));
            }
        } catch (Exception ignored) {
        }
        return Optional.empty();
    }
}
