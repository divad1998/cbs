package com.chirak.cbs.security.student;

import com.chirak.cbs.security.SecurityService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/students/logout")
@AllArgsConstructor
public class StudentLogoutController {
    private final SecurityService securityService;

    @GetMapping
    public void logout(HttpServletResponse response) {
        securityService.removeAuthentication(response);
    }
}