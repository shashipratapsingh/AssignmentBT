package com.bank.user.config;

import com.bank.user.user.User;
import com.bank.user.user.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SeedData {

    @Bean
    CommandLineRunner seedUsers(UserRepository repo, PasswordEncoder encoder) {
        return args -> {
            if (repo.count() > 0) return;

            User u1 = new User();
            u1.setEmail("Shashi@example.com");
            u1.setPassword(encoder.encode("Password@123"));
            u1.setFullName("shashi");
            u1.setPhone("9999999999");
            repo.save(u1);

            User u2 = new User();
            u2.setEmail("shashi@example.com");
            u2.setPassword(encoder.encode("Password@123"));
            u2.setFullName("Shashi Pratap");
            u2.setPhone("12123123123");
            repo.save(u2);
        };
    }
}
