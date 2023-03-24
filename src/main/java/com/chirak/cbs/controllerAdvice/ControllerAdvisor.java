package com.chirak.cbs.controllerAdvice;

import com.chirak.cbs.exception.AffiliateNotFoundException;
import com.chirak.cbs.exception.PasswordException;
import com.chirak.cbs.exception.StudentNotFoundException;
import com.chirak.cbs.exception.TokenException;
import jakarta.mail.MessagingException;
import org.apache.coyote.Response;
import org.hibernate.validator.internal.engine.messageinterpolation.parser.MessageDescriptorFormatException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

@org.springframework.web.bind.annotation.ControllerAdvice
public class ControllerAdvisor {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handle(MethodArgumentNotValidException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(AffiliateNotFoundException.class)
    public ResponseEntity<?> handle(AffiliateNotFoundException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<?> handle(UsernameNotFoundException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(StudentNotFoundException.class)
    public ResponseEntity<?> handle(StudentNotFoundException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<?> handle(DataIntegrityViolationException e) {
        return ResponseEntity.badRequest().body("Either phone number or email or consent letter already exists.");
    }

    @ExceptionHandler(TokenException.class)
    public ResponseEntity<?> handle(TokenException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(MessagingException.class)
    public ResponseEntity<String> handle(MessagingException e) {
        return ResponseEntity.internalServerError().body("Kindly try again.");
    }

    @ExceptionHandler(MissingServletRequestPartException.class)
    public ResponseEntity<String> handle(MissingServletRequestPartException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(PasswordException.class)
    public ResponseEntity<String> handle(PasswordException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
