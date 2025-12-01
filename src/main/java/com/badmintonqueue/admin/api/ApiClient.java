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
    private String displayName;
    private String role;

    public ApiClient(Config config) {
        this.baseUrl = config.getApiBaseUrl();
        this.client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(5))
                .build();
    }

    public boolean hasToken() {
        return token != null && !token.isBlank();
    }

    public void clearAuth() {
        this.token = null;
        this.displayName = null;
        this.role = null;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getRole() {
        return role;
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
            this.displayName = Optional.ofNullable(node.get("displayName")).map(v -> v.asText()).orElse(null);
            this.role = Optional.ofNullable(node.get("role")).map(v -> v.asText()).orElse(null);
            return token != null;
        }
        return false;
    }

    public Optional<TotalsDto> getTotals() {
        if (!hasToken()) return Optional.empty();
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

    public Optional<java.util.List<AdminMatchDto>> getHistory(int take) {
        if (!hasToken()) return Optional.empty();
        try {
            var req = HttpRequest.newBuilder()
                    .uri(URI.create(baseUrl + "/api/admin/history?take=" + take))
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + token)
                    .GET()
                    .build();
            var res = client.send(req, HttpResponse.BodyHandlers.ofString());
            if (res.statusCode() >= 200 && res.statusCode() < 300) {
                var listType = mapper.getTypeFactory().constructCollectionType(java.util.List.class, AdminMatchDto.class);
                java.util.List<AdminMatchDto> items = mapper.readValue(res.body(), listType);
                return Optional.ofNullable(items);
            }
        } catch (Exception ignored) {
        }
        return Optional.empty();
    }
}
