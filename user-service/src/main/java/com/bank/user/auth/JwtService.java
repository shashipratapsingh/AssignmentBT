package com.bank.user.auth;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Map;

@Service
public class JwtService {

    private final JwtEncoder encoder;
    private final String issuer;

    public JwtService(JwtEncoder encoder,
                      @Value("${app.jwt.issuer:banking-app}") String issuer) {
        this.encoder = encoder;
        this.issuer = issuer;
    }

    public String issue(long userId, String email) {
        Instant now = Instant.now();
        var claims = JwtClaimsSet.builder()
                .issuer(issuer)
                .issuedAt(now)
                .expiresAt(now.plus(2, ChronoUnit.HOURS))
                .subject(String.valueOf(userId))
                .claim("uid", userId)
                .claim("email", email)
                .build();

        var headers = JwsHeader.with(MacAlgorithm.HS256).build();
        return encoder.encode(JwtEncoderParameters.from(headers, claims)).getTokenValue();
    }
}
