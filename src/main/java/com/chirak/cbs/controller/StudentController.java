package com.chirak.cbs.controller;

import com.chirak.cbs.dto.StudentDto;
import com.chirak.cbs.dto.UpdatedStudentDto;
import com.chirak.cbs.exception.AffiliateException;
import com.chirak.cbs.exception.PasswordException;
import com.chirak.cbs.exception.StudentException;
import com.chirak.cbs.exception.TokenException;
import com.chirak.cbs.object.ChangePasswordReq;
import com.chirak.cbs.object.Email;
import com.chirak.cbs.service.StudentService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping(path = "/api/students")
@AllArgsConstructor
public class StudentController {
    public final StudentService studentService;

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public void register(@Valid @RequestBody StudentDto studentDto) throws AffiliateException, IOException, MessagingException, StudentException {
        studentService.register(studentDto);
    }

    @GetMapping(path = "/confirm")
    public ResponseEntity<String> confirmEmail(@RequestParam String token) throws TokenException, StudentException {
        studentService.markAsEnabled(token);
        return ResponseEntity.ok("Email confirmed.");
    }

    @GetMapping(path = "/confirm/new-link")
    public ResponseEntity<String> newLink(@RequestParam("token") String token) throws StudentException, MessagingException, TokenException {
        studentService.newConfirmationMail(token);
        return ResponseEntity.ok("Confirmatory mail sent. Check mailbox.");
    }

    @PostMapping(path = "/forgot-password")
    public void forgotPassword(@Valid @RequestBody Email email) throws StudentException, MessagingException {
        studentService.newRandomPassword(email.getEmail());
    }

    @GetMapping(path = "/complete-registration")
    public void confirmRegistration() throws AffiliateException {
        studentService.markAsRegistered();
    }

    @GetMapping
    public StudentDto get() throws StudentException {
        return studentService.get();
    }

    @PatchMapping
    public UpdatedStudentDto update(@Valid @RequestBody UpdatedStudentDto dto) throws IOException, StudentException {

        return studentService.update(dto);
    }

    @PatchMapping(path = "/new-password")
    public void newPassword(@Valid @RequestBody ChangePasswordReq req, HttpServletResponse response) throws PasswordException {
        studentService.newPassword(req, response);
    }

    @PatchMapping(path = "/new-email")
    public void newEmail(@Valid @RequestBody Email email, HttpServletResponse response) throws MessagingException, StudentException {
        studentService.changeEmail(email.getEmail(), response);
    }

    @DeleteMapping
    public void delete(HttpServletResponse response) {
        studentService.delete(response);
    }
}