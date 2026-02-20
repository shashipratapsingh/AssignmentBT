package com.bank.account.account;

import com.bank.account.account.dto.CreateAccountRequest;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    private final AccountRepository repo;
    private final AccountNumberGenerator generator;

    public AccountController(AccountRepository repo, AccountNumberGenerator generator) {
        this.repo = repo;
        this.generator = generator;
    }

    @PostMapping
    public ResponseEntity<Account> create(Authentication auth, @Valid @RequestBody CreateAccountRequest req) {
        long userId = currentUserId(auth);

        Account a = new Account();
        a.setUserId(userId);
        a.setAccountType(req.accountType());

        // make sure generated account number is unique
        String acctNo;
        int tries = 0;
        do {
            acctNo = generator.next10();
            tries++;
        } while (repo.existsByAccountNumber(acctNo) && tries < 10);

        if (repo.existsByAccountNumber(acctNo)) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Could not generate account number, try again");
        }

        a.setAccountNumber(acctNo);
        a.setBalance(new BigDecimal("1000.00"));

        Account saved = repo.save(a);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @GetMapping
    public List<Account> list(Authentication auth) {
        long userId = currentUserId(auth);
        return repo.findByUserId(userId);
    }

    @GetMapping("/{id}")
    public Account get(Authentication auth, @PathVariable long id) {
        long userId = currentUserId(auth);
        Account acc = repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found"));
        if (acc.getUserId() != userId) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Account does not belong to you");
        }
        return acc;
    }

    @GetMapping("/{id}/balance")
    public Map<String, Object> balance(Authentication auth, @PathVariable long id) {
        Account acc = get(auth, id);
        return Map.of("accountId", acc.getId(), "balance", acc.getBalance());
    }

    @GetMapping("/by-number/{accountNumber}")
    public Account findByNumber(@PathVariable String accountNumber) {
        return repo.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "To account not found"));
    }

    private long currentUserId(Authentication auth) {
        Jwt jwt = (Jwt) auth.getPrincipal();
        Object uid = jwt.getClaim("uid");
        if (uid instanceof Integer i) return i.longValue();
        if (uid instanceof Long l) return l;
        return Long.parseLong(String.valueOf(uid));
    }
}
