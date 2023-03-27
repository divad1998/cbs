package com.chirak.cbs.controller;

import com.chirak.cbs.dto.StudentDto;
import com.chirak.cbs.dto.UpdatedStudentDto;
import com.chirak.cbs.exception.*;
import com.chirak.cbs.object.ChangePasswordReq;
import com.chirak.cbs.object.Email;
import com.chirak.cbs.object.ForgotPasswordReq;
import com.chirak.cbs.security.AuthenticationService;
import com.chirak.cbs.service.StudentService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletResponse;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping(path = "/api/students")
@AllArgsConstructor
public class StudentController {
    public final StudentService studentService;
    private final AuthenticationService authService;

    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @ResponseStatus(value = HttpStatus.CREATED)
    public void register(@Valid @RequestPart("studentDto") StudentDto studentDto,
                                        @RequestPart("parentConsentLetter") MultipartFile consentLetter,
                                        @RequestPart("idDocument") MultipartFile idDoc) throws AffiliateException, IOException, MessagingException {
        studentService.register(studentDto, consentLetter, idDoc);
    }

    @GetMapping(path = "/confirm")
    public void confirmEmail(@RequestParam String token) throws TokenException, StudentException {
        studentService.markAsEnabled(token);
    }

    @PostMapping(path = "/forgot-password")
    public void requestForgotPasswordMail(@Valid @RequestBody Email body) throws MessagingException, StudentException {
        studentService.requestForgotPasswordMail(body.getEmail());
    }

    @GetMapping(path = "/forgot-password")
    public void validatePasswordResetToken(@RequestParam String token) throws TokenException {
        studentService.validatePasswordResetToken(token);
    }

    @PostMapping(path = "/forgot-password/reset")
    public void resetPassword(@Valid @RequestBody ForgotPasswordReq resetReq) throws StudentException {
        studentService.resetPassword(resetReq);
    }

    @GetMapping(path = "/complete-registration")
    public void confirmRegistration() throws StudentException, AffiliateException {
        studentService.markAsRegistered();
    }

    @GetMapping
    public StudentDto get() throws StudentException {
        return studentService.get();
    }

    @PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public UpdatedStudentDto update(@Valid @RequestPart("updatedStudentDto") UpdatedStudentDto dto,
                                                                @RequestPart("parentConsentLetter") MultipartFile consentLetter,
                                                                @RequestPart("idDocument") MultipartFile idDoc) throws IOException {

        return studentService.update(dto, consentLetter, idDoc);
    }

    @PatchMapping(path = "/new-password")
    public void newPassword(@Valid @RequestBody ChangePasswordReq req) throws PasswordException {
        studentService.newPassword(req);
    }

    @DeleteMapping
    public void delete(HttpServletResponse response) {
        studentService.delete(response);
    }
}