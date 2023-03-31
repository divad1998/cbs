package com.chirak.cbs.controller;

import com.chirak.cbs.dto.AffiliateDto;
import com.chirak.cbs.dto.StudentDto;
import com.chirak.cbs.dto.UpdatedAffiliateDto;
import com.chirak.cbs.exception.AffiliateException;
import com.chirak.cbs.exception.TokenException;
import com.chirak.cbs.object.ChangePasswordReq;
import com.chirak.cbs.object.Deduction;
import com.chirak.cbs.object.Email;
import com.chirak.cbs.service.AffiliateService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/affiliates")
@AllArgsConstructor
public class AffiliateController {
    private final AffiliateService affiliateService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void register(@Valid @RequestBody AffiliateDto affiliateDto) throws MessagingException, AffiliateException {
        affiliateService.create(affiliateDto);
    }

    @GetMapping(path = "/confirm")
    public ResponseEntity<String> confirmEmail(@RequestParam("token") String token) throws TokenException, AffiliateException {
        affiliateService.markAsEnabled(token);
        return ResponseEntity.ok("Email confirmed.");
    }

    @GetMapping(path = "/confirm/new-link")
    public ResponseEntity<String> newLink(@RequestParam("token") String token) throws AffiliateException, MessagingException, TokenException {
        affiliateService.newConfirmationMail(token);
        return ResponseEntity.ok("Confirmatory mail sent. Check mailbox.");
    }

    @PostMapping(path = "/forgot-password")
    public void forgotPassword(@Valid @RequestBody Email email) throws MessagingException, AffiliateException {
        affiliateService.newRandomPassword(email.getEmail());
    }

    @GetMapping(path = "/referrals")
    public List<StudentDto> getReferrals() {
        return affiliateService.getReferrals();
    }

    @GetMapping
    public AffiliateDto get() {
        return affiliateService.get();
    }

    @PatchMapping
    public UpdatedAffiliateDto update(@Valid @RequestBody UpdatedAffiliateDto dto) throws AffiliateException {
        return affiliateService.update(dto);
    }

    @GetMapping(path = "/balance")
    public String getBalance() {
        return affiliateService.getBalance();

    }

    @PatchMapping(path = "/new-email")
    public void newEmail(@Valid @RequestBody Email email, HttpServletResponse response) throws MessagingException, AffiliateException {
        affiliateService.changeEmail(email.getEmail(), response);
    }

    @PatchMapping(path = "/new-password")
    public void newPassword(@Valid @RequestBody ChangePasswordReq req, HttpServletResponse response) {
        affiliateService.newPassword(req, response);
    }

    @PatchMapping("/balance/deduction")
    public void afterPayOut(@Valid @RequestBody Deduction deduction) {
        affiliateService.deduct(deduction.getAmount());
    }

    @DeleteMapping
    public void delete(HttpServletResponse response) {
        affiliateService.delete(response);
    }
}