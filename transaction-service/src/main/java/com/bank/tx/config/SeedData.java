package com.bank.tx.config;

import com.bank.tx.tx.Transaction;
import com.bank.tx.tx.TransactionRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.time.Instant;

@Configuration
public class SeedData {

    @Bean
    CommandLineRunner seedTransactions(TransactionRepository repo) {
        return args -> {
            if (repo.count() > 0) return;

            // sample transactions (account ids assume account-service inserts 1..3)
            repo.save(make(1L, 3L, "50.00"));
            repo.save(make(2L, 3L, "25.50"));
            repo.save(make(1L, 2L, "10.00"));
            repo.save(make(2L, 1L, "5.00"));
            repo.save(make(1L, 3L, "100.00"));
        };
    }

    private Transaction make(Long from, Long to, String amount) {
        Transaction t = new Transaction();
        t.setFromAccountId(from);
        t.setToAccountId(to);
        t.setAmount(new BigDecimal(amount));
        t.setTransactionType("TRANSFER");
        t.setCreatedAt(Instant.now());
        return t;
    }
}
