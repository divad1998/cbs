//package com.chirak.cbs.unitTests.service;
//
//import com.chirak.cbs.exception.InvalidTokenException;
//import com.chirak.cbs.exception.TokenExpiredException;
//import com.chirak.cbs.object.Token;
//import com.chirak.cbs.repository.TokenRepository;
//import com.chirak.cbs.service.TokenService;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.time.Instant;
//import java.util.Date;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.anyString;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(value = {MockitoExtension.class})
//public class TokenServiceSpec {
//
//    @Mock
//    TokenRepository tokenRepo;
//
//    @InjectMocks
//    TokenService tokenService;
//
//    Token token;
//
//    @BeforeEach
//    void init() {
//        token = new Token();
//        token.setToken("RandString");
//    }
//
//    @DisplayName("Generates and returns token")
//    @Test
//    void generateToken() {
//        when(tokenRepo.save(any())).thenReturn(token);
//
//        String generatedToken = tokenService.generateToken("Student's email");
//
//        Assertions.assertEquals(token.getToken(), generatedToken);
//
//        Mockito.verify(tokenRepo, times(1)).save(any());
//    }
//
//    @DisplayName("Generates random string containing 7 integers.")
//    @Test
//    void generateRandomString() {
//        Assertions.assertEquals(7, tokenService.generateRandomString().length());
//    }
//
//    @DisplayName("Validates valid token.")
//    @Test
//    void validateToken() throws InvalidTokenException, TokenExpiredException { //ToDo: validate null token, invalid and valid token
//        when(tokenRepo.findByToken("blah")).thenReturn(token);
//        token.setExpiresAt(Date.from(Instant.now().plusMillis(10000L)));
//
//        tokenService.validate("blah");
//
//        verify(tokenRepo, times(1)).findByToken("blah");
//    }
//}