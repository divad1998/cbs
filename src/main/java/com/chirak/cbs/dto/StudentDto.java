package com.chirak.cbs.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class StudentDto {
    @NotBlank //A value must be entered, and it shouldn't include whitespace characters only.
    @NotEmpty
    private String firstName;

    private String middleName; //no validation

    @NotBlank
    @NotEmpty
    private String lastName;

    @NotBlank
    @NotEmpty
    private String sex;

    private int age; //numbers only

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
    @Email(message = "Invalid email format.") //must be a valid email format
    private String email;

    @NotBlank
    @NotEmpty
    private String password;

    private byte[] parentConsentLetter; //required
    private byte[] idDocument; //required

    @NotBlank
    @NotEmpty
    private String healthStatus;

    @NotBlank
    @NotEmpty
    private String guarantorFullName;

    private int guarantorAge; //numbers only

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

    private String referralCode; // not required
    private String regNumber; //won't be visible in the registration page
    private boolean registered; //not visible too
    private boolean admitted; //same
    private boolean graduated; //same
}