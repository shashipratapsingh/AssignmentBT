package com.bank.account.config;

import com.bank.account.account.Account;
import com.bank.account.account.AccountRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;

@Configuration
public class SeedData {

    @Bean
    CommandLineRunner seedAccounts(AccountRepository repo) {
        return args -> {
            if (repo.count() > 0) return;

            // assumes user-service seeds ids 1 and 2
            Account a1 = new Account();
            a1.setUserId(1L);
            a1.setAccountType("SAVINGS");
            a1.setAccountNumber("1234567890");
            a1.setBalance(new BigDecimal("2500.00"));
            repo.save(a1);

            Account a2 = new Account();
            a2.setUserId(1L);
            a2.setAccountType("CURRENT");
            a2.setAccountNumber("2345678901");
            a2.setBalance(new BigDecimal("1500.00"));
            repo.save(a2);

            Account a3 = new Account();
            a3.setUserId(2L);
            a3.setAccountType("SAVINGS");
            a3.setAccountNumber("3456789012");
            a3.setBalance(new BigDecimal("3200.00"));
            repo.save(a3);
        };
    }
}
