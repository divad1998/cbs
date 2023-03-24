package com.chirak.cbs.unitTests.service;

import com.chirak.cbs.entity.Student;
import com.chirak.cbs.exception.StudentNotFoundException;
import com.chirak.cbs.repository.StudentRepository;
import com.chirak.cbs.service.EmailService;
import com.chirak.cbs.service.StudentService;
import com.chirak.cbs.unitTests.extension.StudentParameterResolver;
import jakarta.mail.MessagingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;

@ExtendWith({MockitoExtension.class, StudentParameterResolver.class})
public class StudentServiceSpec {
    Student student;

    @Mock
    StudentRepository studentRepo;

    @Mock
    EmailService emailService;

    @InjectMocks
    StudentService studentService;

    @BeforeEach
    void init(Student resolvedStudent) {
        this.student = resolvedStudent;
    }

    @DisplayName("Calls EmailService send")
    @Test
    void sendForgotPasswordMail() throws StudentNotFoundException, MessagingException {
        Mockito.when(studentRepo.findByEmail("student's email")).thenReturn(Optional.of(student));

        studentService.requestForgotPasswordMail("student's email");

        Mockito.verify(studentRepo, Mockito.times(1)).findByEmail("student's email");
    }

    @DisplayName("Gets student by id")
    @Test
    void getStudentById() {
        // no args
        // stub
    }

}