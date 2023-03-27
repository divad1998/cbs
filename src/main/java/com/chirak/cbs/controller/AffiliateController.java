package com.chirak.cbs.controller;

import com.chirak.cbs.dto.AffiliateDto;
import com.chirak.cbs.dto.StudentDto;
import com.chirak.cbs.dto.UpdatedAffiliateDto;
import com.chirak.cbs.exception.AffiliateException;
import com.chirak.cbs.exception.TokenException;
import com.chirak.cbs.object.Email;
import com.chirak.cbs.object.ForgotPasswordReq;
import com.chirak.cbs.service.AffiliateService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/affiliates")
@AllArgsConstructor
public class AffiliateController {
    private final AffiliateService affiliateService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void register(@Valid @RequestBody AffiliateDto affiliateDto) throws MessagingException {
        affiliateService.create(affiliateDto);
    }

    @GetMapping(path = "/confirm")
    public void confirmEmail(@RequestParam("token") String token) throws TokenException, AffiliateException {
        affiliateService.markAsEnabled(token);
    }

    @PostMapping(path = "/forgot-password")
    public void forgotPassword(@Valid @RequestBody Email email) throws MessagingException, AffiliateException {
        affiliateService.requestForgotPasswordMail(email);
    }

    @GetMapping(path = "/forgot-password")
    public void validatePasswordResetToken(@RequestParam String token) throws TokenException {
        affiliateService.validatePasswordResetToken(token);
    }

    @PostMapping(path = "/forgot-password/reset")
    public void resetPassword(@Valid @RequestBody ForgotPasswordReq req) throws AffiliateException {
        affiliateService.resetPassword(req);
    }

    @GetMapping(path = "/referrals")
    public List<StudentDto> getReferrals() {
        return affiliateService.getReferrals();
    }

    @GetMapping
    public AffiliateDto get() {
        return affiliateService.get();
    }

    @PutMapping
    public UpdatedAffiliateDto update(@Valid @RequestBody UpdatedAffiliateDto dto) {
        return affiliateService.update(dto);
    }

    //Test: what happens to referralCode in student's object after delete?
    @DeleteMapping
    public void delete(HttpServletResponse response) {
        affiliateService.delete(response);
    }
}