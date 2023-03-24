package com.chirak.cbs.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/logout")
@AllArgsConstructor
public class LogoutController {
    private final SecurityService securityService;

    @GetMapping
    public void logout(HttpServletResponse response) {
        securityService.removeAuthentication(response);
    }
}