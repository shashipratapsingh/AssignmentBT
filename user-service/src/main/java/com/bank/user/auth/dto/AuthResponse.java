package com.bank.user.auth.dto;

public record AuthResponse(
        String token,
        long userId,
        String email,
        String fullName
) {}
