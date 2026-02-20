package com.bank.tx.tx.dto;

public record TransferResponse(
        long transactionId,
        long fromAccountId,
        long toAccountId,
        String message
) {}
