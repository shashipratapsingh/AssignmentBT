package com.bank.user.auth.dto;

import jakarta.validation.constraints.*;

public record RegisterRequest(
        @Email @NotBlank String email,
        @NotBlank @Size(min = 8, max = 72) String password,
        @NotBlank String fullName,
        @NotBlank @Size(min = 10, max = 15) String phone
) {}
