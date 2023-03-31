package com.chirak.cbs.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UpdatedStudentDto {

    @NotBlank
    @NotEmpty
    private String firstName;

    private String middleName;

    @NotBlank
    @NotEmpty
    private String lastName;

    @NotBlank
    @NotEmpty
    private String gender;

    private int age;

    @NotBlank
    @NotEmpty
    private String stateOfOrigin;

    @NotBlank
    @NotEmpty
    private String address;

    @NotBlank
    @NotEmpty
    private String phoneNumber;

    @NotBlank
    @NotEmpty
    private String studyCenter;

    @NotBlank
    @NotEmpty
    private String healthStatus;

    private String regNumber;
    private boolean registered;
    private boolean admitted;
    private boolean graduated;
    private String referralCode;
}