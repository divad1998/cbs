package com.chirak.cbs.object;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class Email {

    @NotBlank(message = "Invalid email.")
    @jakarta.validation.constraints.Email(message = "Invalid email format.")
    private String email;
}
