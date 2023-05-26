package com.ml.testsexamples.dto;

import jakarta.validation.constraints.Email;
import lombok.NonNull;

import java.math.BigDecimal;

public record BankAccountDto(
        @Email(message = "Invalid email format") @NonNull String accountId,
        @NonNull String firstName,
        @NonNull String lastName,
        @NonNull BigDecimal balance,
        @NonNull BigDecimal minimumBalance,
        boolean active) {
}