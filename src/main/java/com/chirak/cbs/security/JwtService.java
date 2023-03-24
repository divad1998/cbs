package com.chirak.cbs.security;

import com.chirak.cbs.config.ApplicationProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.time.Instant;
import java.util.Date;

import static java.time.Instant.now;
import static java.util.Date.from;

@Service
@AllArgsConstructor
public class JwtService {
    private final ApplicationProperties properties;

    public String generateToken(String subject) {
        return Jwts
                .builder()
                .setSubject(subject)
                .setIssuedAt(from(now())) //this is essentially redundant
                .setExpiration(from(now().plusMillis(Long.parseLong(properties.getExpiresAfter()))))
                .signWith(signingKey())
                .compact();
    }

    private Key signingKey() {
        byte[] base64 = Decoders.BASE64.decode(properties.getSecret_key());
        return Keys.hmacShaKeyFor(base64);
    }


    /**
     * Extract all claims from jwt.
     * @param token
     * @return
     */
    public Claims extractClaims(String token) {
        return Jwts
                        .parserBuilder()
                        .setSigningKey(signingKey())
                        .build()
                        .parseClaimsJws(token)
                        .getBody();
    }

    /**
     * Extracts username from jwt claims.
     * @param token
     * @return
     */
    public String extractUsername(String token) {
        return extractClaims(token).getSubject();
    }

    public boolean isExpired(String token) {
        return extractClaims(token)
                        .getExpiration()
                        .before(Date.from(Instant.now()));
    }
}