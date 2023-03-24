package com.chirak.cbs.object;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class ChangePasswordReq {

    @NotBlank
    @NotEmpty
    private String newPassword;

    @NotBlank
    @NotEmpty
    private String confirmedPassword;
}