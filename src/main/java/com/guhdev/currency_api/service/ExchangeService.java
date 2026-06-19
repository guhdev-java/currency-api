package com.guhdev.currency_api.service;

import com.guhdev.currency_api.dto.ConvertResponse;
import com.guhdev.currency_api.dto.ExchangeRateApiResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ExchangeService {

    private final RestClient restClient;
    private final String baseUrl;

    // cache simples em memória: base -> (rates, loadedAt)
    private final Map<String, CachedRates> cache = new ConcurrentHashMap<>();
    private static final long CACHE_TTL_MILLIS = 5 * 60 * 1000; // 5 min

    public ExchangeService(
            RestClient.Builder builder,
            @Value("${exchange.api.base-url}") String baseUrl
    ) {
        this.restClient = builder.build();
        this.baseUrl = baseUrl;
    }

    public ConvertResponse convert(String from, String to, BigDecimal amount) {
        String fromUpper = from.toUpperCase();
        String toUpper = to.toUpperCase();

        ExchangeRateApiResponse data = getRates(fromUpper);

        if (data == null || data.rates() == null) {
            throw new IllegalArgumentException("Não foi possível obter taxas para: " + fromUpper);
        }

        BigDecimal rate = data.rates().get(toUpper);
        if (rate == null) {
            throw new IllegalArgumentException("Moeda de destino não suportada: " + toUpper);
        }

        BigDecimal converted = amount.multiply(rate).setScale(2, RoundingMode.HALF_UP);

        return new ConvertResponse(
                fromUpper,
                toUpper,
                amount,
                rate,
                converted,
                Instant.now()
        );
    }

    private ExchangeRateApiResponse getRates(String base) {
        CachedRates cached = cache.get(base);
        long now = System.currentTimeMillis();

        if (cached != null && (now - cached.loadedAtMillis) < CACHE_TTL_MILLIS) {
            return cached.payload;
        }

        ExchangeRateApiResponse response = restClient.get()
                .uri(baseUrl + "/" + base)
                .retrieve()
                .body(ExchangeRateApiResponse.class);

        if (response == null || !"success".equalsIgnoreCase(response.result())) {
            throw new IllegalArgumentException("Falha ao consultar taxa para base: " + base);
        }

        cache.put(base, new CachedRates(response, now));
        return response;
    }

    private record CachedRates(ExchangeRateApiResponse payload, long loadedAtMillis) {
    }
}