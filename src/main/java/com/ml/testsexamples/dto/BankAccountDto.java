package com.ml.testsexamples.dto;

import java.math.BigDecimal;

public record BankAccountDto(
        String firstName,
        String lastName,
        BigDecimal balance,
        BigDecimal minimumBalance,
        boolean active) {
}