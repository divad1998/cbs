package com.chirak.cbs.controller;

import com.chirak.cbs.dto.StudentDto;
import com.chirak.cbs.dto.UpdatedStudentDto;
import com.chirak.cbs.exception.AffiliateException;
import com.chirak.cbs.exception.PasswordException;
import com.chirak.cbs.exception.StudentException;
import com.chirak.cbs.exception.TokenException;
import com.chirak.cbs.object.ChangePasswordReq;
import com.chirak.cbs.object.Email;
import com.chirak.cbs.object.Response;
import com.chirak.cbs.service.StudentService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping(path = "/api/students")
@AllArgsConstructor
public class StudentController {
    public final StudentService studentService;

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public Response register(@Valid @RequestBody StudentDto studentDto) throws AffiliateException, IOException, MessagingException, StudentException {
        studentService.register(studentDto);
        return new Response(true, "New student created. Kindly check your email to verify account.", null);
    }

    @GetMapping(path = "/confirm")
    public Response confirmEmail(@RequestParam String token) throws TokenException, StudentException {
        studentService.markAsEnabled(token);
        return new Response(true, "Email confirmed.", null);
    }

    @GetMapping(path = "/confirm/new-link")
    public Response newLink(@RequestParam("token") String token) throws StudentException, MessagingException, TokenException {
        studentService.newConfirmationMail(token);
        return new Response(true, "Confirmation mail sent. Check mailbox.", null);
    }

    @PostMapping(path = "/forgot-password")
    public void forgotPassword(@Valid @RequestBody Email email) throws StudentException, MessagingException {
        studentService.newRandomPassword(email.getEmail());
    }

    @GetMapping(path = "/complete-registration")
    public Response confirmRegistration() throws AffiliateException {
        studentService.markAsRegistered();
        return new Response(true, "Student's registration complete.", null);
    }

    @GetMapping
    public Response get() throws StudentException {
        StudentDto studentDto = studentService.get();
        return new Response(true, "Student fetched.", studentDto);
    }

    @PatchMapping
    public Response update(@Valid @RequestBody UpdatedStudentDto dto) throws IOException, StudentException {

        UpdatedStudentDto updatedDto = studentService.update(dto);
        return new Response(true, "Student updated.", updatedDto);
    }

    @PatchMapping(path = "/new-password")
    public Response newPassword(@Valid @RequestBody ChangePasswordReq req, HttpServletResponse response) throws PasswordException {
        studentService.newPassword(req, response);
        return new Response(true, "Password updated.", null);
    }

    @PatchMapping(path = "/new-email")
    public Response newEmail(@Valid @RequestBody Email email, HttpServletResponse response) throws MessagingException, StudentException {
        studentService.changeEmail(email.getEmail(), response);
        return new Response(true, "Email changed successfully. Check mailbox to verify email.", null);
    }

    @DeleteMapping
    public Response delete(HttpServletResponse response) {
        studentService.delete(response);
        return new Response(true, "Student deleted successfully.", null);
    }
}