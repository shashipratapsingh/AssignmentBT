package com.bank.account.account;

import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;

@Service
public class AccountService {

    private final AccountRepository repo;

    public AccountService(AccountRepository repo) {
        this.repo = repo;
    }

    @Transactional
    public Account debitForUser(long accountId, long userId, BigDecimal amount) {
        validateAmount(amount);

        Account acc = repo.findById(accountId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found"));

        if (acc.getUserId() != userId) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You can only transfer from your own accounts");
        }

        if (acc.getBalance().compareTo(amount) < 0) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Insufficient funds");
        }

        acc.setBalance(acc.getBalance().subtract(amount));
        return repo.save(acc);
    }

    @Transactional
    public Account credit(long accountId, BigDecimal amount) {
        validateAmount(amount);

        Account acc = repo.findById(accountId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found"));

        acc.setBalance(acc.getBalance().add(amount));
        return repo.save(acc);
    }

    private void validateAmount(BigDecimal amount) {
        if (amount == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Amount is required");
        }
        if (amount.scale() > 2) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Amount must have max 2 decimal places");
        }
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Amount must be greater than 0");
        }
    }
}
