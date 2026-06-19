package com.guhdev.currency_api.dto;

import java.math.BigDecimal;
import java.util.Map;

public record ExchangeRateApiResponse (
        String result,
        String base_code,
        Map<String, BigDecimal> rates
) {}
