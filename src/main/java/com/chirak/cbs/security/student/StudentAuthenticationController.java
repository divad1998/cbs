package com.chirak.cbs.security.student;

import com.chirak.cbs.security.AuthenticationRequest;
import com.chirak.cbs.security.AuthenticationService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/students/auth")
@AllArgsConstructor
public class StudentAuthenticationController {
    private final AuthenticationService authService;

    @PostMapping
    public void authenticate(@Valid @RequestBody AuthenticationRequest authRequest, HttpServletResponse response) {
        authService.authenticateStudent(authRequest, response);
    }
}
