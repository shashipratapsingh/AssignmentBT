package com.bank.tx.tx.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.math.BigDecimal;

public record TransferRequest(
        @NotNull Long fromAccountId,
        @NotBlank @Pattern(regexp = "\\d{10}", message = "must be a 10-digit account number") String toAccountNumber,
        @NotNull BigDecimal amount
) {}
