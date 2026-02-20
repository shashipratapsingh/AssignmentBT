package com.bank.tx.tx;

import com.bank.tx.tx.dto.TransferRequest;
import com.bank.tx.tx.dto.TransferResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@CrossOrigin(origins = "*")
public class TransactionController {

    private final TransferService transferService;
    private final TransactionRepository repo;

    public TransactionController(TransferService transferService, TransactionRepository repo) {
        this.transferService = transferService;
        this.repo = repo;
    }

    @PostMapping("/transfer")
    public TransferResponse transfer(@RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader,
                                     @Valid @RequestBody TransferRequest req) {
        Transaction t = transferService.transfer(req, authHeader);
        return new TransferResponse(t.getId(), t.getFromAccountId(), t.getToAccountId(),
                "Transfer completed successfully");
    }

    @GetMapping("/account/{accountId}")
    public List<Transaction> history(@PathVariable long accountId) {
        return repo.findByFromAccountIdOrToAccountIdOrderByCreatedAtDesc(accountId, accountId);
    }
}
