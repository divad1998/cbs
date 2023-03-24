package com.chirak.cbs.object;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class ForgotPasswordReq
{
    @Email(message = "Invalid email format")
    @NotBlank
    @NotEmpty
    private String email;

    @NotBlank
    @NotEmpty
    private String password;

    @NotBlank
    @NotEmpty
    private String confirmedPassword;
}
