package com.chirak.cbs.service;

import com.chirak.cbs.dto.StudentDto;
import com.chirak.cbs.dto.UpdatedStudentDto;
import com.chirak.cbs.entity.Affiliate;
import com.chirak.cbs.entity.Student;
import com.chirak.cbs.exception.*;
import com.chirak.cbs.mapper.StudentMapper;
import com.chirak.cbs.object.ChangePasswordReq;
import com.chirak.cbs.object.ForgotPasswordReq;
import com.chirak.cbs.repository.StudentRepository;
import com.chirak.cbs.security.SecurityService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;

@Service
public class StudentService {
    private final AffiliateService affiliateService;
    private final StudentRepository studentRepo;
    private final EmailService emailService;
    private final TokenService tokenService;
    private final SecurityService securityService;

    private final StudentMapper studentMapper = StudentMapper.instance;

    @Value("${cbs.students.confirmEmail.baseUrl}")
    private String baseUrl1;

    @Value("${cbs.students.resetPassword.validateToken.baseUrl}")
    private String baseUrl2;



    public StudentService(AffiliateService affiliateService,
                          StudentRepository studentRepo,
                          EmailService emailService,
                          TokenService tokenService,
                          SecurityService securityService) {
        this.affiliateService = affiliateService;
        this.studentRepo = studentRepo;
        this.emailService = emailService;
        this.tokenService = tokenService;
        this.securityService = securityService;
    }


    @Transactional
    public void register(StudentDto studentDto, MultipartFile consentLetter, MultipartFile idDoc) throws IOException, AffiliateException, MessagingException {
        studentDto.setParentConsentLetter(consentLetter.getBytes());
        studentDto.setIdDocument(idDoc.getBytes());
        Student student = studentMapper.toStudent(studentDto);
        student.setPassword(studentDto.getPassword());
        if (studentDto.getReferralCode() != null) {
            student.setAffiliate(affiliateService.findByReferralCode(studentDto.getReferralCode()));
        }
        Student savedStudent = studentRepo.save(student);
        //send email confirmation mail
        emailService.sendEmailConfirmationMail(savedStudent.getFirstName(), savedStudent.getLastName(), savedStudent.getEmail(), baseUrl1);
    }

    /**
     * Enables student after email confirmation.
     * @param token
     * @throws TokenException
     * @throws StudentException
     */
    public void markAsEnabled(String token) throws TokenException, StudentException {
        tokenService.validate(token);
        Student student = studentRepo.findByEmail(token.substring(7)).orElseThrow(StudentService::returnException);
        student.setEnabled(true);
        studentRepo.save(student);
    }

    //ToDo: what if student hits this without paying? is there a way for the frontend to call this endpoint without it being shown on the browser? if yes, good!
    public void markAsRegistered() {
        Student student = securityService.authenticatedStudent();
        student.setRegistered(true);
        student.setRegNumber("CBS-" + LocalDate.now().getYear() + "-" + student.getId());
        Student savedStudent = studentRepo.save(student);
        //increase balance of Affiliate and persist
        Affiliate affiliate = savedStudent.getAffiliate();
        if (affiliate != null) {
            affiliate.increaseBalance(500);
            affiliateService.afterBalanceIncrease(affiliate);
        }
    }

    public void requestForgotPasswordMail(String email) throws StudentException, MessagingException, StudentException {
        Student student = studentRepo
                                            .findByEmail(email)
                                            .orElseThrow(StudentService::returnException);

        if (student.isEnabled()) {
            emailService.sendForgotPasswordMail(student.getFirstName(), student.getLastName(), student.getEmail(), baseUrl2);
        } else {
            throw new StudentException("Email wasn't confirmed after registration.");
        }
    }

    /**
     * Validates generated token.
     * @param token
     */
    public void validatePasswordResetToken(String token) throws TokenException {
        tokenService.validate(token);
    }

    public void resetPassword(ForgotPasswordReq resetReq) throws StudentException {
        Student student = studentRepo
                                                .findByEmail(resetReq.getEmail())
                                                .orElseThrow(StudentService::returnException);
        if (student.isEnabled()) {
            student.setPassword(resetReq.getPassword());
            studentRepo.save(student);
        } else {
            throw new StudentException("Email wasn't confirmed after registration.");
        }
    }

    /**
     * Returns authenticated student.
     * @return
     */
    public StudentDto get() {
        Student student = securityService.authenticatedStudent();
        StudentDto studentDto = studentMapper.toDto(student);
        studentDto.setPassword(null);
        if (student.getAffiliate() != null) {
            studentDto.setReferralCode(student.getAffiliate().getReferralCode());
        }

        return studentDto;
    }

    public UpdatedStudentDto update(UpdatedStudentDto dto, MultipartFile consentLetter, MultipartFile idDoc) throws IOException {
        dto.setParentConsentLetter(consentLetter.getBytes());
        dto.setIdDocument(idDoc.getBytes());
        var student = studentMapper.toStudent(dto);
        var authStudent = securityService.authenticatedStudent();
        student.setId(authStudent.getId());
        student.setPassword(authStudent.getPassword());
        student.setEnabled(authStudent.isEnabled());
        student.setAffiliate(authStudent.getAffiliate());

        var savedStudent = studentRepo.save(student);
        //map back to dto
        var updatedDto = studentMapper.toUpdatedStudentDto(savedStudent);
        if (savedStudent.getAffiliate() != null) {
            updatedDto.setReferralCode(savedStudent.getAffiliate().getReferralCode());
        }

        return updatedDto;
    }

    /**
     * Permits password change when user is logged in.
     * @param req
     * @throws PasswordException
     */
    public void newPassword(ChangePasswordReq req) throws PasswordException {
        var authStudent = securityService.authenticatedStudent();
        authStudent.setPassword(req.getNewPassword());
        studentRepo.save(authStudent);
    }

    public void delete(HttpServletResponse response) {
        var student = securityService.authenticatedStudent();
        studentRepo.deleteById(student.getId());
        //remove authentication
        securityService.removeAuthentication(response);
    }

    /**
     * Supplies {@link StudentException}.
     *
     * @return
     */
    private static StudentException returnException() {
        return new StudentException("Invalid email.");
    }
}