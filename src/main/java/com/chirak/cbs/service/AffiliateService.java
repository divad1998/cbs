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
import com.chirak.cbs.object.Email;
import com.chirak.cbs.object.ForgotPasswordReq;
import com.chirak.cbs.repository.AffiliateRepository;
import com.chirak.cbs.security.SecurityService;
import com.chirak.cbs.security.student.StudentDetailsService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AffiliateService {
    private final AffiliateRepository affiliateRepo;
    private final EmailService emailService;
    private final TokenService tokenService;
    private final SecurityService securityService;
    private final AffiliateMapper mapper = AffiliateMapper.instance;
    private final StudentMapper studentMapper = StudentMapper.instance;

    @Value("${cbs.affiliates.confirmEmail.baseUrl}")
    private String baseUrl1;

    @Value("${cbs.affiliates.resetPassword.validateToken.baseUrl}")
    private String baseUrl2;

    public AffiliateService(AffiliateRepository repo,
                            EmailService emailService,
                            TokenService tokenService, SecurityService securityService) {
        this.affiliateRepo = repo;
        this.emailService = emailService;
        this.tokenService = tokenService;
        this.securityService = securityService;
    }

    @Transactional
    public void create(AffiliateDto affiliateDto) throws MessagingException {
        var affiliate = mapper.to_Affiliate(affiliateDto);
        affiliate.setPassword(affiliateDto.getPassword());
        var savedAffiliate = affiliateRepo.save(affiliate);

        //now I can send confirmatory email
        emailService.sendEmailConfirmationMail(savedAffiliate.getFirstName(), savedAffiliate.getLastName(), savedAffiliate.getEmail(), baseUrl1);
    }

    public void markAsEnabled(String token) throws TokenException, AffiliateException {
        tokenService.validate(token);
        var email = token.substring(7);
        var affiliate = affiliateRepo.findByEmail(email).orElseThrow(AffiliateService::supplyException);
        affiliate.setEnabled(true);
        affiliate.setReferralCode("cbs-" + affiliate.getFirstName() + "-" + affiliate.getId());
        affiliateRepo.save(affiliate);
    }

    public void afterBalanceIncrease(Affiliate affiliate) {
        affiliateRepo.save(affiliate);
    }

    public Affiliate findByReferralCode(String referralCode) throws AffiliateException {
        //return affiliate with referral code
        Affiliate affiliate = affiliateRepo.findByReferralCode(referralCode).orElseThrow(AffiliateService::supplyException);
        return affiliate;
    }

    public void requestForgotPasswordMail(Email email) throws AffiliateException, MessagingException {
        Affiliate affiliate = affiliateRepo.findByEmail(email.getEmail()).orElseThrow(AffiliateService::supplyException);
        if (affiliate.isEnabled()) {
            emailService.sendForgotPasswordMail(affiliate.getFirstName(), affiliate.getLastName(), affiliate.getEmail(), baseUrl2);
        } else {
            throw new AffiliateException("Email wasn't confirmed after registration.");
        }
    }

    public void validatePasswordResetToken(String token) throws TokenException {
        tokenService.validate(token);
    }

    public void resetPassword(ForgotPasswordReq req) throws AffiliateException {
        Affiliate affiliate = affiliateRepo.findByEmail(req.getEmail()).orElseThrow(AffiliateService::supplyException);
        if (affiliate.isEnabled()) {
            affiliate.setPassword(req.getPassword());
            affiliateRepo.save(affiliate);
        } else {
            throw new AffiliateException("Email wasn't confirmed after registration.");
        }
    }

    public List<StudentDto> getReferrals() {
        Affiliate authAffiliate = securityService.authenticatedAffiliate();
        List<Student> students = authAffiliate.getStudents();
        List<StudentDto> studentDtoList = new ArrayList<>();
        if (!students.isEmpty()) {
            for (Student student : students) {
                studentDtoList.add(studentMapper.toDto(student));
            }
        }

        return studentDtoList;
    }

    public AffiliateDto get() {
        Affiliate affiliate = securityService.authenticatedAffiliate();
        AffiliateDto affiliateDto = mapper.to_AffiliateDto(affiliate);
        return affiliateDto;
    }

    public UpdatedAffiliateDto update(UpdatedAffiliateDto dto) {
        Affiliate affiliate = mapper.to_Affiliate(dto);
        Affiliate authAffiliate = securityService.authenticatedAffiliate();

        affiliate.setId(authAffiliate.getId());
        affiliate.setPassword(authAffiliate.getPassword());
        affiliate.setBalance(authAffiliate.getBalance());
        affiliate.setEmail(authAffiliate.getEmail());
        affiliate.setStudents(authAffiliate.getStudents());
        affiliate.setEnabled(authAffiliate.isEnabled());

        Affiliate savedAffiliate = affiliateRepo.save(affiliate);
        return mapper.to_UpdatedAffiliateDto(savedAffiliate);
    }

    public void delete(HttpServletResponse response) {
        Affiliate authAffiliate = securityService.authenticatedAffiliate();
        affiliateRepo.delete(authAffiliate);

        securityService.removeAuthentication(response);
    }

    private static AffiliateException supplyException() {
        return new AffiliateException("Invalid affiliate.");
    }

}