package com.bank.user.user;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @GetMapping("/me")
    public Map<String, Object> me(Authentication auth) {
        Jwt jwt = (Jwt) auth.getPrincipal();
        return Map.of(
                "userId", jwt.getClaimAsString("uid"),
                "email", jwt.getClaimAsString("email")
        );
    }
}
