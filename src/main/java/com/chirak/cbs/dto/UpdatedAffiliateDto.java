package com.chirak.cbs.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class UpdatedAffiliateDto {

    @NotBlank
    @NotEmpty
    private String firstName;

    @NotBlank
    @NotEmpty
    private String lastName;

    @NotBlank
    @NotEmpty
    private String sex;

    @NotBlank
    @NotEmpty
    private String phoneNumber;

    @NotBlank
    @NotEmpty
    private String referralCode;
}