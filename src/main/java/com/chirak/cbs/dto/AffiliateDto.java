package com.chirak.cbs.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class AffiliateDto {

    @NotBlank //A value must be entered, and it shouldn't include whitespace characters only.
    @NotEmpty
    private String firstName; //

    @NotBlank
    @NotEmpty
    private String lastName; //

    @NotBlank
    @NotEmpty
    private String gender; //

    @NotBlank
    @NotEmpty
    private String phoneNumber; //

    @NotBlank
    @NotEmpty
    @Email
    private String email;

    @NotBlank
    @NotEmpty
    private String password; //

    //No need for balance
    private String referralCode; //not visible in registration form

}