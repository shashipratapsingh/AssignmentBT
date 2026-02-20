package com.bank.tx.tx;

import com.bank.tx.clients.AccountClient;
import com.bank.tx.tx.dto.TransferRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.Map;

@Service
public class TransferService {

    private final AccountClient accounts;
    private final TransactionRepository txRepo;

    public TransferService(AccountClient accounts, TransactionRepository txRepo) {
        this.accounts = accounts;
        this.txRepo = txRepo;
    }

    @Transactional
    public Transaction transfer(TransferRequest req, String authHeader) {
        validate(req);

        try {
            Map fromAcc = accounts.getAccountById(req.fromAccountId(), authHeader).block();
            if (fromAcc == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "From account not found");

            Map toAcc = accounts.getAccountByNumber(req.toAccountNumber(), authHeader).block();
            if (toAcc == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "To account not found");

            long toAccountId = Long.parseLong(String.valueOf(toAcc.get("id")));
            if (toAccountId == req.fromAccountId()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "From and To accounts must be different");
            }

            // Step 1: debit
            accounts.debit(req.fromAccountId(), req.amount(), authHeader).block();

            // Step 2: credit (if this fails, try refund)
            try {
                accounts.credit(toAccountId, req.amount(), authHeader).block();
            } catch (Exception creditFail) {
                // best-effort refund
                try {
                    accounts.credit(req.fromAccountId(), req.amount(), authHeader).block();
                } catch (Exception refundFail) {
                    throw new ResponseStatusException(HttpStatus.CONFLICT,
                            "Transfer partially failed (debit succeeded but credit/refund failed). Contact support.");
                }
                throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE,
                        "Credit failed. Amount was refunded. Please retry.");
            }

            Transaction t = new Transaction();
            t.setFromAccountId(req.fromAccountId());
            t.setToAccountId(toAccountId);
            t.setAmount(req.amount());
            t.setTransactionType("TRANSFER");
            return txRepo.save(t);

        } catch (WebClientResponseException ex) {
            // propagate meaningful downstream errors when possible
            if (ex.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found");
            }
            if (ex.getStatusCode() == HttpStatus.CONFLICT) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, ex.getResponseBodyAsString().contains("Insufficient")
                        ? "Insufficient funds"
                        : "Conflict while processing transfer");
            }
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Account service unavailable");
        } catch (ResponseStatusException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Transfer failed");
        }
    }

    private void validate(TransferRequest req) {
        if (req.fromAccountId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "fromAccountId is required");
        }
        BigDecimal amt = req.amount();
        if (amt == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "amount is required");
        }
        if (amt.scale() > 2) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Amount must have max 2 decimal places");
        }
        if (amt.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Amount must be greater than 0");
        }
    }
}
