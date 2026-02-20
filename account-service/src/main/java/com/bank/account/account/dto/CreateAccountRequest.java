package com.bank.account.account.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record CreateAccountRequest(
        @NotBlank
        @Pattern(regexp = "SAVINGS|CURRENT", message = "must be SAVINGS or CURRENT")
        String accountType
) {}
