package com.chirak.cbs.controllerAdvice;

import com.chirak.cbs.exception.AffiliateException;
import com.chirak.cbs.exception.PasswordException;
import com.chirak.cbs.exception.StudentException;
import com.chirak.cbs.exception.TokenException;
import jakarta.mail.MessagingException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ControllerAdvisor {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handle(MethodArgumentNotValidException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(AffiliateException.class)
    public ResponseEntity<?> handle(AffiliateException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<?> handle(UsernameNotFoundException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(StudentException.class)
    public ResponseEntity<?> handle(StudentException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(TokenException.class)
    public ResponseEntity<?> handle(TokenException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(MessagingException.class)
    public ResponseEntity<String> handle(MessagingException e) {
        return ResponseEntity.internalServerError().body("Kindly check email and try again.");
    }

    @ExceptionHandler(PasswordException.class)
    public ResponseEntity<String> handle(PasswordException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<String> handle(BadCredentialsException e) {
        return ResponseEntity.badRequest().body("Either email or password is invalid.");
    }

    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<String> handle(DisabledException e) {
        return ResponseEntity.status(403).body("Email is yet to be confirmed. Check your mailbox for confirmation link.");
    }
}