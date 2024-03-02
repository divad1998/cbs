package com.chirak.cbs.controllerAdvice;

import com.chirak.cbs.exception.AffiliateException;
import com.chirak.cbs.exception.PasswordException;
import com.chirak.cbs.exception.StudentException;
import com.chirak.cbs.exception.TokenException;
import com.chirak.cbs.object.Response;
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
        return ResponseEntity.badRequest().body(new Response(false, e.getMessage(), null));
    }

    @ExceptionHandler(AffiliateException.class)
    public ResponseEntity<?> handle(AffiliateException e) {
        return ResponseEntity.badRequest().body(new Response(false, e.getMessage(), null));
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<?> handle(UsernameNotFoundException e) {
        return ResponseEntity.badRequest().body(new Response(false, e.getMessage(), null));
    }

    @ExceptionHandler(StudentException.class)
    public ResponseEntity<?> handle(StudentException e) {
        return ResponseEntity.badRequest().body(new Response(false, e.getMessage(), null));
    }

    @ExceptionHandler(TokenException.class)
    public ResponseEntity<?> handle(TokenException e) {
        return ResponseEntity.badRequest().body(new Response(false, e.getMessage(), null));
    }

//    @ExceptionHandler(MessagingException.class)
//    public ResponseEntity<String> handle(MessagingException e) {
//        return ResponseEntity.internalServerError().body(e.getMessage());
//    }

    @ExceptionHandler(PasswordException.class)
    public ResponseEntity<Response> handle(PasswordException e) {
        return ResponseEntity.badRequest().body(new Response(false, e.getMessage(), null));
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Response> handle(BadCredentialsException e) {
        return ResponseEntity.status(401).body(new Response(false, "Either email or password is invalid.", null));
    }

    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<Response> handle(DisabledException e) {
        return ResponseEntity.status(403).body(new Response(false, "Email is yet to be confirmed. Check your mailbox for confirmation link.", null));
    }
}