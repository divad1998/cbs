package com.chirak.cbs.security;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AuthenticationRequest {
    @NotBlank(message = "Invalid email.")
    @Email(message = "Invalid email format.")
    private String email;

    @NotBlank
    private String password;
}
