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
    private String sex;

    private int age;

    @NotBlank
    @NotEmpty
    private String stateOfOrigin;

    @NotBlank
    @NotEmpty
    private String lga;

    @NotBlank
    @NotEmpty
    private String city;

    @NotBlank
    @NotEmpty
    private String residentialAddress;


    @NotBlank
    @NotEmpty
    private String phoneNumber;
    @NotBlank
    @NotEmpty
    private String studyCenter;

    @NotBlank
    @NotEmpty
    @Email(message = "Invalid email format.")
    private String email;

    private byte[] parentConsentLetter;

    @NotBlank
    @NotEmpty
    private String guarantorFullName;

    private int guarantorAge;

    @NotBlank
    @NotEmpty
    private String guarantorSex;

    @NotBlank
    @NotEmpty
    private String guarantorResidentialAddress;

    @NotBlank
    @NotEmpty
    private String guarantorPhoneNumber;

    @NotBlank
    @NotEmpty
    @Email(message = "Invalid email format.")
    private String guarantorEmail;

    private String regNumber;
    private boolean registered;
    private boolean admitted;
    private boolean graduated;
    private String affiliateId;
}
