package com.chirak.cbs.controller;

import com.chirak.cbs.dto.AffiliateDto;
import com.chirak.cbs.dto.StudentDto;
import com.chirak.cbs.dto.UpdatedAffiliateDto;
import com.chirak.cbs.exception.AffiliateException;
import com.chirak.cbs.exception.TokenException;
import com.chirak.cbs.object.ChangePasswordReq;
import com.chirak.cbs.object.Deduction;
import com.chirak.cbs.object.Email;
import com.chirak.cbs.object.Response;
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
    public Response register(@Valid @RequestBody AffiliateDto affiliateDto) throws MessagingException, AffiliateException {
        affiliateService.create(affiliateDto);
        return new Response(true, "Success. Check mailbox to complete registration.", null);
    }

    @GetMapping(path = "/confirm")
    public Response confirmEmail(@RequestParam("token") String token) throws TokenException, AffiliateException {
        affiliateService.markAsEnabled(token);
        return new Response(true, "Email confirmed.", null);
    }

    @GetMapping(path = "/confirm/new-link")
    public Response newLink(@RequestParam("token") String token) throws AffiliateException, MessagingException, TokenException {
        affiliateService.newConfirmationMail(token);
        return new Response(true, "Confirmation mail sent. Check mailbox.", null);
    }

    @PostMapping(path = "/forgot-password")
    public Response forgotPassword(@Valid @RequestBody Email email) throws MessagingException, AffiliateException {
        affiliateService.newRandomPassword(email.getEmail());
        return new Response(true, "Success. Check mailbox.", null);
    }

    @GetMapping(path = "/referrals")
    public Response getReferrals() {

        List<StudentDto> studentDtoList = affiliateService.getReferrals();
        return new Response(true, "Success. Referrals fetched.", studentDtoList);
    }

    @GetMapping
    public Response get() {

        AffiliateDto affiliateDto = affiliateService.get();
        return new Response(true, "Success.", affiliateDto);
    }

    @PatchMapping
    public Response update(@Valid @RequestBody UpdatedAffiliateDto dto) throws AffiliateException {
        UpdatedAffiliateDto updatedDto = affiliateService.update(dto);
        return new Response(true, "Success. Affiliate updated.", updatedDto);
    }

    @GetMapping(path = "/balance")
    public Response getBalance() {

        String balance = affiliateService.getBalance();
        return new Response(true, "Success. Balance fetched.", balance);
    }

    @PatchMapping(path = "/new-email")
    public Response newEmail(@Valid @RequestBody Email email, HttpServletResponse response) throws MessagingException, AffiliateException {
        affiliateService.changeEmail(email.getEmail(), response);
        return new Response(true, "Email changed.", null);
    }

    @PatchMapping(path = "/new-password")
    public Response newPassword(@Valid @RequestBody ChangePasswordReq req, HttpServletResponse response) {
        affiliateService.newPassword(req, response);
        return new Response(true, "Success. Password changed.", null);
    }

    @PatchMapping("/balance/deduction")
    public Response afterPayOut(@Valid @RequestBody Deduction deduction) {
        affiliateService.deduct(deduction.getAmount());
        return new Response(true, "Success.", null);
    }

    @DeleteMapping
    public Response delete(HttpServletResponse response) {
        affiliateService.delete(response);
        return new Response(true, "Success. Affiliate deleted.", null);
    }
}