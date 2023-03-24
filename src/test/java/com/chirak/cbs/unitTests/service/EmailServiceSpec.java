package com.chirak.cbs.unitTests.service;

import com.chirak.cbs.service.EmailService;
import com.chirak.cbs.service.TokenService;
import jakarta.mail.MessagingException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;

import static org.mockito.ArgumentMatchers.anyString;

@ExtendWith(value = MockitoExtension.class)
public class EmailServiceSpec {

    @Mock
    TokenService tokenService;

    @Mock
    JavaMailSender mailSender;

    @InjectMocks
    EmailService emailService;

    @DisplayName("Sends email to client's email.")
    @Test
    void sendEmail() throws MessagingException {
        Mockito.when(tokenService.generateToken(anyString())).thenReturn("token string");

        emailService.sendForgotPasswordMail("david", "dinneya", "student's email");

        Mockito.verify(tokenService, Mockito.times(1)).generateToken(anyString());
        Mockito.verify(mailSender);
    }
}
