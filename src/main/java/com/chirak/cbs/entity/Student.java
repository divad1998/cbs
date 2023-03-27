package com.chirak.cbs.entity;

import com.chirak.cbs.role.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;

@Entity
@Data
public class Student implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String middleName;
    private String lastName;
    private String sex;
    private int age;
    private String stateOfOrigin;
    private String lga;
    private String city;
    private String residentialAddress;

    @Column(unique = true)
    private String phoneNumber;

    private String studyCenter;

    @Column(unique = true)
    private String email;
    private String password;

    @Column(unique = true)
    private byte[] parentConsentLetter;

    @Column(unique = true)
    private byte[] idDocument;

    private String healthStatus;
    private String guarantorFullName;
    private int guarantorAge;
    private String guarantorSex;
    private String guarantorResidentialAddress;
    private String guarantorPhoneNumber;
    private String guarantorEmail;

    @Column(unique = true)
    private String regNumber;

    @Enumerated(EnumType.STRING)
    private Role role = Role.STUDENT;

    private boolean enabled;
    private boolean registered;
    private boolean admitted;
    private boolean graduated;

    @ManyToOne
    @JoinColumn(referencedColumnName = "id")
    private Affiliate affiliate;

    private LocalDateTime createdOn;

    public void setPassword(String password) {
        this.password = new BCryptPasswordEncoder().encode(password);
    }

    @PrePersist
    public void setCreatedOn() {
        createdOn = LocalDateTime.now();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority("ROLE_" + getRole().name()));
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}