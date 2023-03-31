package com.chirak.cbs.service;

import com.chirak.cbs.exception.TokenException;
import com.chirak.cbs.object.Token;
import com.chirak.cbs.repository.TokenRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
@AllArgsConstructor
public class TokenService {
    private final TokenRepository tokenRepo;

    public String generateToken(String email) {
        Token token = new Token();
        token.setToken(generateRandomString().concat(email));
        token.setExpiresAt(token.getCreatedAt().plusMinutes(15L));

        return tokenRepo
                        .save(token)
                        .getToken();
    }

    public String generateRandomString() {
        var rand = new Random();
        String str = "";
        int count = 1;
        while (count <= 7) {
            str = str.concat(String.valueOf(rand.nextInt(10)));
            count++;
        }
        return str;
    }

    /**
     * Validates token.
     * @param token
     * @throws TokenException
     *
     */
    public void validate(String token) throws TokenException {
        Token persistedToken = tokenRepo.findByToken(token).orElseThrow(TokenService::supplyException);
        if (persistedToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new TokenException("Expired link.");
        } else {
                tokenRepo.delete(persistedToken);
        }
    }

    public Optional<Token> getToken(String token) {
        return tokenRepo.findByToken(token);
    }

    private static TokenException supplyException() {
        return new TokenException("Invalid token.");
    }

    public void delete(Token token) {
        tokenRepo.delete(token);
    }
}