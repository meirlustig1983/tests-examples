package com.ml.testsexamples.dto;

import com.ml.testsexamples.enums.TransactionType;
import lombok.NonNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransactionDto(
        @NonNull BigDecimal amount,
        @NonNull TransactionType type,
        @NonNull LocalDateTime createdAt) {
}