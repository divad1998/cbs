package com.chirak.cbs.service;

import com.chirak.cbs.dto.StudentDto;
import com.chirak.cbs.dto.UpdatedStudentDto;
import com.chirak.cbs.entity.Affiliate;
import com.chirak.cbs.entity.Student;
import com.chirak.cbs.exception.AffiliateException;
import com.chirak.cbs.exception.PasswordException;
import com.chirak.cbs.exception.StudentException;
import com.chirak.cbs.exception.TokenException;
import com.chirak.cbs.mapper.StudentMapper;
import com.chirak.cbs.object.ChangePasswordReq;
import com.chirak.cbs.object.Token;
import com.chirak.cbs.repository.AffiliateRepository;
import com.chirak.cbs.repository.StudentRepository;
import com.chirak.cbs.security.SecurityService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.Base64;
import java.util.List;

@Service
public class StudentService {
    private final AffiliateRepository affiliateRepo;
    private final StudentRepository studentRepo;
    private final EmailService emailService;
    private final TokenService tokenService;
    private final SecurityService securityService;

    private final StudentMapper studentMapper = StudentMapper.instance;

    @Value("${cbs.students.confirmEmail.baseUrl}")
    private String baseUrl1;

    @Value("${cbs.students.confirmEmail.newLink.baseUrl}")
    private String baseUrl2;

    public StudentService(AffiliateRepository affiliateRepo,
                          StudentRepository studentRepo,
                          EmailService emailService,
                          TokenService tokenService,
                          SecurityService securityService) {
        this.affiliateRepo = affiliateRepo;
        this.studentRepo = studentRepo;
        this.emailService = emailService;
        this.tokenService = tokenService;
        this.securityService = securityService;
    }

    public void register(StudentDto studentDto) throws AffiliateException, StudentException, MessagingException {
        //Early return
        if (studentRepo.findByEmail(studentDto.getEmail()).isEmpty() && studentRepo.findByPhoneNumber(studentDto.getPhoneNumber()).isEmpty()) {
            var student = studentMapper.toStudent(studentDto);
            student.setPassword(studentDto.getPassword());
            var referralCode = studentDto.getReferralCode();
            Student savedStudent;
            if (referralCode != null) {
                if (affiliateRepo.findByReferralCode(referralCode).isPresent()) {
                    savedStudent = studentRepo.save(student);
                } else {
                    throw new AffiliateException("Invalid referral code.");
                }
            } else {
                savedStudent = studentRepo.save(student);
            }
            //send confirmation mail
            emailService.sendEmailConfirmationMail(savedStudent.getFirstName(), savedStudent.getLastName(), savedStudent.getEmail(), baseUrl1, baseUrl2);

        } else {
            throw new StudentException("Either email or phone number already exists.");
        }
    }

    public void newConfirmationMail(String token) throws StudentException, MessagingException, TokenException {
        if (tokenService.getToken(token).isPresent()) {
            Token persistedToken = tokenService.getToken(token).get();

            String encodedEmail = token.substring(7);
            byte[] decodedBytes = Base64.getDecoder().decode(encodedEmail);
            String decodedEmail = new String(decodedBytes, StandardCharsets.UTF_8);

            Student student = studentRepo.findByEmail(decodedEmail).orElseThrow(StudentService::returnException);
            emailService.sendEmailConfirmationMail(student.getFirstName(), student.getLastName(), student.getEmail(), baseUrl1, baseUrl2);

            tokenService.delete(persistedToken);
        } else {
            throw new TokenException("Invalid token.");
        }
    }

    /**
     * Enables student after email confirmation.
     * @param token
     * @throws TokenException
     * @throws StudentException
     */
    public void markAsEnabled(String token) throws TokenException, StudentException {
        tokenService.validate(token);
        String encodedEmail = token.substring(7);
        byte[] decodedBytes = Base64.getDecoder().decode(encodedEmail);
        String decodedEmail = new String(decodedBytes, StandardCharsets.UTF_8);

        Student student = studentRepo.findByEmail(decodedEmail).orElseThrow(StudentService::returnException);
        student.setEnabled(true);
        studentRepo.save(student);
    }

    //ToDo: what if student hits this without paying? is there a way for the frontend to call this endpoint without it being shown on the browser? if yes, good!
    public void markAsRegistered() throws AffiliateException {
        Student student = securityService.authenticatedStudent();
        student.setRegistered(true);
        student.setRegNumber("CBS-" + LocalDate.now().getYear() + "-" + student.getId());
        Student savedStudent = studentRepo.save(student);
        //increase balance of Affiliate and persist
        var referralCode = savedStudent.getReferralCode();
        if (affiliateRepo.findByReferralCode(referralCode).isPresent()) {
            //increase balance of affiliate
            Affiliate affiliate = affiliateRepo.findByReferralCode(referralCode).get();
            affiliate.increaseBalance(500);
            affiliateRepo.save(affiliate);
        }
        //Do nothing if affiliate's account has been deleted.
    }

    @Transactional
    public void newRandomPassword(String email) throws StudentException, MessagingException {
        Student student = studentRepo.findByEmail(email).orElseThrow(StudentService::returnException);
        if (student.isEnabled()) {
            String newPassword = tokenService.generateRandomString();
            student.setPassword(newPassword);
            Student savedStudent = studentRepo.save(student);

            emailService.sendForgotPasswordMail(savedStudent.getFirstName(), savedStudent.getLastName(), savedStudent.getEmail(), newPassword);
        } else {
            throw new StudentException("Email wasn't confirmed after registration");
        }
    }

    /**
     * Returns authenticated student.
     * @return
     */
    public StudentDto get() {
        Student student = securityService.authenticatedStudent();
        return studentMapper.toDto(student);
    }

    public UpdatedStudentDto update(UpdatedStudentDto dto) throws IOException, StudentException {
        var authStudent = securityService.authenticatedStudent();
        if ((!authStudent.getPhoneNumber().equals(dto.getPhoneNumber())) && studentRepo.findByPhoneNumber(dto.getPhoneNumber()).isPresent()) {
            throw new StudentException("Phone number already exists.");

        } else {
            authStudent.setFirstName(dto.getFirstName());
            authStudent.setMiddleName(dto.getMiddleName());
            authStudent.setLastName(dto.getLastName());
            authStudent.setGender(dto.getGender());
            authStudent.setAge(dto.getAge());
            authStudent.setStateOfOrigin(dto.getStateOfOrigin());
            authStudent.setAddress(dto.getAddress());
            authStudent.setPhoneNumber(dto.getPhoneNumber());
            authStudent.setStudyCenter(dto.getStudyCenter());
            authStudent.setHealthStatus(dto.getHealthStatus());

            var savedStudent = studentRepo.save(authStudent);
            //map back to dto
            return studentMapper.toUpdatedStudentDto(savedStudent);
        }
    }

    /**
     * Permits password change when user is logged in.
     * @param req
     * @throws PasswordException
     */
    public void newPassword(ChangePasswordReq req, HttpServletResponse response) throws PasswordException {
        var authStudent = securityService.authenticatedStudent();
        authStudent.setPassword(req.getNewPassword());
        studentRepo.save(authStudent);
        //log student out
        securityService.removeAuthentication(response);
    }

    public void delete(HttpServletResponse response) {
        var student = securityService.authenticatedStudent();
        studentRepo.deleteById(student.getId());
        //remove authentication
        securityService.removeAuthentication(response);
    }

    public List<Student> getByReferralCode(String referralCode) {
        return studentRepo.findByReferralCode(referralCode);
    }

    public void changeEmail(String email, HttpServletResponse response) throws MessagingException, StudentException {
        if (studentRepo.findByEmail(email).isEmpty()) {
            Student authenticatedStudent = securityService.authenticatedStudent();
            authenticatedStudent.setEmail(email);
            authenticatedStudent.setEnabled(false);
            Student savedStudent = studentRepo.save(authenticatedStudent);

            //send another confirmation mail
            emailService.sendEmailConfirmationMail(savedStudent.getFirstName(), savedStudent.getLastName(), savedStudent.getEmail(), baseUrl1, baseUrl2);
            //logout student
            securityService.removeAuthentication(response);
        } else {
            throw new StudentException("Email already exists."); //familiar response
        }
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