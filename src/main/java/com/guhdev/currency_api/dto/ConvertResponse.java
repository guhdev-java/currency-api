package com.guhdev.currency_api.dto;

import java.math.BigDecimal;
import java.time.Instant;

public record ConvertResponse (
        String from,
        String to,
        BigDecimal amount,
        BigDecimal rate,
        BigDecimal convertedAmount,
        Instant timestamp
){}
