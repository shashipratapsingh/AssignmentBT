package com.bank.account.account;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/accounts/internal")
public class InternalBalanceController {

    private final AccountService service;

    public InternalBalanceController(AccountService service) {
        this.service = service;
    }

    @PostMapping("/{accountId}/debit")
    public Account debit(Authentication auth, @PathVariable long accountId, @RequestParam BigDecimal amount) {
        long userId = currentUserId(auth);
        return service.debitForUser(accountId, userId, amount);
    }

    @PostMapping("/{accountId}/credit")
    public Account credit(@PathVariable long accountId, @RequestParam BigDecimal amount) {
        // credit can be done for any account (transfer destination)
        return service.credit(accountId, amount);
    }

    private long currentUserId(Authentication auth) {
        Jwt jwt = (Jwt) auth.getPrincipal();
        Object uid = jwt.getClaim("uid");
        if (uid instanceof Integer i) return i.longValue();
        if (uid instanceof Long l) return l;
        return Long.parseLong(String.valueOf(uid));
    }
}
