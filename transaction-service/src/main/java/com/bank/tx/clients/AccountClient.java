package com.bank.tx.clients;

import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.*;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.Map;

@Component
public class AccountClient {

    private final WebClient webClient;

    public AccountClient(WebClient.Builder builder) {
        this.webClient = builder
                .baseUrl("http://localhost:8082")
                .build();
    }

    public Mono<Map> getAccountById(long id, String authHeader) {
        return webClient.get()
                .uri("/api/accounts/{id}", id)
                .header(HttpHeaders.AUTHORIZATION, authHeader)
                .retrieve()
                .bodyToMono(Map.class)
                .timeout(Duration.ofSeconds(3));
    }

    public Mono<Map> getAccountByNumber(String number, String authHeader) {
        return webClient.get()
                .uri("/api/accounts/by-number/{num}", number)
                .header(HttpHeaders.AUTHORIZATION, authHeader)
                .retrieve()
                .bodyToMono(Map.class)
                .timeout(Duration.ofSeconds(3));
    }

    public Mono<Map> debit(long accountId, BigDecimal amount, String authHeader) {
        return webClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/accounts/internal/{id}/debit")
                        .queryParam("amount", amount)
                        .build(accountId))
                .header(HttpHeaders.AUTHORIZATION, authHeader)
                .retrieve()
                .bodyToMono(Map.class)
                .timeout(Duration.ofSeconds(3));
    }

    public Mono<Map> credit(long accountId, BigDecimal amount, String authHeader) {
        return webClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/accounts/internal/{id}/credit")
                        .queryParam("amount", amount)
                        .build(accountId))
                .header(HttpHeaders.AUTHORIZATION, authHeader)
                .retrieve()
                .bodyToMono(Map.class)
                .timeout(Duration.ofSeconds(3));
    }
}
