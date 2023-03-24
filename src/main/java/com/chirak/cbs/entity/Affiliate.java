package com.chirak.cbs.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Entity
@Data
public class Affiliate {
    @Id
    private String affiliateId;
    private String firstName;
    private int balance;


    @NotBlank(message = "Invalid email.")
    @Email(message = "Invalid email format.")
    private String email;

    public void increaseBalance(int referralBonus) {
        balance += referralBonus;
    }
}
