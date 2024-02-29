package com.chirak.cbs.service;

import com.chirak.cbs.dto.AffiliateDto;
import com.chirak.cbs.dto.StudentDto;
import com.chirak.cbs.dto.UpdatedAffiliateDto;
import com.chirak.cbs.entity.Affiliate;
import com.chirak.cbs.entity.Student;
import com.chirak.cbs.exception.AffiliateException;
import com.chirak.cbs.exception.TokenException;
import com.chirak.cbs.mapper.AffiliateMapper;
import com.chirak.cbs.mapper.StudentMapper;
import com.chirak.cbs.object.ChangePasswordReq;
import com.chirak.cbs.object.Token;
import com.chirak.cbs.repository.AffiliateRepository;
import com.chirak.cbs.security.SecurityService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Service
public class AffiliateService {
    private final AffiliateRepository affiliateRepo;
    private final EmailService emailService;
    private final TokenService tokenService;
    private final SecurityService securityService;
    private final StudentService studentService;
    private final AffiliateMapper mapper = AffiliateMapper.instance;
    private final StudentMapper studentMapper = StudentMapper.instance;

    @Value("${cbs.affiliates.confirmEmail.baseUrl}")
    private String baseUrl1;

    @Value("${cbs.affiliates.confirmEmail.newLink.baseUrl}")
    private String baseUrl2;

    public AffiliateService(AffiliateRepository repo,
                            EmailService emailService,
                            TokenService tokenService, SecurityService securityService, StudentService studentService) {
        this.affiliateRepo = repo;
        this.emailService = emailService;
        this.tokenService = tokenService;
        this.securityService = securityService;
        this.studentService = studentService;
    }

    @Transactional
    public void create(AffiliateDto affiliateDto) throws MessagingException, AffiliateException {
        if (affiliateRepo.findByEmail(affiliateDto.getEmail()).isEmpty() && affiliateRepo.findByPhoneNumber(affiliateDto.getPhoneNumber()).isEmpty()) {
            var affiliate = mapper.to_Affiliate(affiliateDto);
            affiliate.setPassword(affiliateDto.getPassword());
            var savedAffiliate = affiliateRepo.save(affiliate);
            //send confirmation email
            emailService.sendEmailConfirmationMail(savedAffiliate.getFirstName(), savedAffiliate.getLastName(), savedAffiliate.getEmail(), baseUrl1, baseUrl2);
        } else {
            throw new AffiliateException("Either phone number or email already exists.");
        }
    }

    /**
     * Triggers a new 'confirm email' mail when link is expired.
     * @param token
     * @throws AffiliateException
     * @throws MessagingException
     */
    public void newConfirmationMail(String token) throws AffiliateException, MessagingException, TokenException {
        if (tokenService.getToken(token).isPresent()) {
            //get token entity
            Token persistedToken = tokenService.getToken(token).get();

            String encodedEmail = token.substring(7);
            byte[] decodedBytes = Base64.getDecoder().decode(encodedEmail);
            String decodedEmail = new String(decodedBytes, StandardCharsets.UTF_8);
            Affiliate affiliate = affiliateRepo.findByEmail(decodedEmail).orElseThrow(AffiliateService::supplyException);
            emailService.sendEmailConfirmationMail(affiliate.getFirstName(), affiliate.getLastName(), affiliate.getEmail(), baseUrl1, baseUrl2);

            tokenService.delete(persistedToken);
        } else {
            throw new TokenException("Invalid token.");
        }
    }

    public void markAsEnabled(String token) throws TokenException, AffiliateException {
        tokenService.validate(token);
        var encodedEmail = token.substring(7);
        byte[] decodedBytes = Base64.getDecoder().decode(encodedEmail);
        String decodedEmail = new String(decodedBytes, StandardCharsets.UTF_8);

        var affiliate = affiliateRepo.findByEmail(decodedEmail).orElseThrow(AffiliateService::supplyException);
        affiliate.setEnabled(true);
        affiliate.setReferralCode("cbs-" + affiliate.getFirstName() + "-" + affiliate.getId());
        affiliateRepo.save(affiliate);
    }

    public void newRandomPassword(String email) throws AffiliateException, MessagingException {
        Affiliate affiliate = affiliateRepo.findByEmail(email).orElseThrow(AffiliateService::supplyException);
        if (affiliate.isEnabled()) {
            String newPassword = tokenService.generateRandomString();
            affiliate.setPassword(newPassword);
            Affiliate savedAffiliate = affiliateRepo.save(affiliate);

            emailService.sendForgotPasswordMail(savedAffiliate.getFirstName(), savedAffiliate.getLastName(), savedAffiliate.getEmail(), newPassword);
        } else {
            throw new AffiliateException("Email wasn't confirmed after registration.");
        }
    }

    public List<StudentDto> getReferrals() {
        Affiliate authAffiliate = securityService.authenticatedAffiliate();
        var referralCode = authAffiliate.getReferralCode();
        List<Student> students = studentService.getByReferralCode(referralCode);
        List<StudentDto> studentDtoList = new ArrayList<>();
        if (!students.isEmpty()) {
            for (Student student : students) {
                StudentDto studentDto = studentMapper.toDto(student);
                studentDtoList.add(studentDto);
            }
        }

        return studentDtoList;
    }

    public AffiliateDto get() {
        Affiliate affiliate = securityService.authenticatedAffiliate();
        return mapper.to_AffiliateDto(affiliate);
    }

    public UpdatedAffiliateDto update(UpdatedAffiliateDto dto) throws AffiliateException {
        Affiliate authAffiliate = securityService.authenticatedAffiliate();
        if ((!authAffiliate.getPhoneNumber().equals(dto.getPhoneNumber())) && affiliateRepo.findByPhoneNumber(dto.getPhoneNumber()).isPresent()) {
            throw new AffiliateException("Phone number already exists.");

        } else {
            //set new fields on authAffiliate
            authAffiliate.setFirstName(dto.getFirstName());
            authAffiliate.setLastName(dto.getLastName());
            authAffiliate.setGender(dto.getGender());
            authAffiliate.setPhoneNumber(dto.getPhoneNumber());

            var savedAffiliate = affiliateRepo.save(authAffiliate);
            return mapper.to_UpdatedAffiliateDto(savedAffiliate);
        }
    }

    public String getBalance() {
        var authAffiliate = securityService.authenticatedAffiliate();
        return "N" + authAffiliate.getBalance();
    }

    public void delete(HttpServletResponse response) {
        Affiliate authAffiliate = securityService.authenticatedAffiliate();
        affiliateRepo.deleteById(authAffiliate.getId());

        securityService.removeAuthentication(response);
    }

    public void changeEmail(String email, HttpServletResponse response) throws MessagingException, AffiliateException {
        if (affiliateRepo.findByEmail(email).isEmpty()) {
            Affiliate authenticatedAffiliate = securityService.authenticatedAffiliate();
            authenticatedAffiliate.setEmail(email);
            authenticatedAffiliate.setEnabled(false);
            Affiliate savedAffiliate = affiliateRepo.save(authenticatedAffiliate);
            //send mail
            emailService.sendEmailConfirmationMail(savedAffiliate.getFirstName(), savedAffiliate.getLastName(), savedAffiliate.getEmail(), baseUrl1, baseUrl2);
            //logout affiliate
            securityService.removeAuthentication(response);

        } else {
            throw new AffiliateException("Email already exists.");
        }
    }

    public void newPassword(ChangePasswordReq req, HttpServletResponse response) {
        var authenticatedAffiliate = securityService.authenticatedAffiliate();
        authenticatedAffiliate.setPassword(req.getNewPassword());
        affiliateRepo.save(authenticatedAffiliate);
        //log affiliate out
        securityService.removeAuthentication(response);
    }

    public void deduct(int amount) {
        var authAffiliate = securityService.authenticatedAffiliate();
        var balance = authAffiliate.getBalance();
        balance = balance - amount;
        authAffiliate.setBalance(balance);

        affiliateRepo.save(authAffiliate);
    }

    private static AffiliateException supplyException() {
        return new AffiliateException("Invalid Email.");
    }
}