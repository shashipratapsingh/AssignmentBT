package com.bank.account.account;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component
public class AccountNumberGenerator {
    private final SecureRandom rnd = new SecureRandom();

    public String next10() {
        int first = 1 + rnd.nextInt(9);
        StringBuilder sb = new StringBuilder().append(first);
        for (int i = 0; i < 9; i++) sb.append(rnd.nextInt(10));
        return sb.toString();
    }
}
