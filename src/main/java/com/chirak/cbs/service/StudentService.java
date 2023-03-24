package com.chirak.cbs.service;

import com.chirak.cbs.dto.StudentDto;
import com.chirak.cbs.dto.UpdatedStudentDto;
import com.chirak.cbs.entity.Affiliate;
import com.chirak.cbs.entity.Student;
import com.chirak.cbs.exception.AffiliateNotFoundException;
import com.chirak.cbs.exception.PasswordException;
import com.chirak.cbs.exception.StudentNotFoundException;
import com.chirak.cbs.exception.TokenException;
import com.chirak.cbs.mapper.StudentMapper;
import com.chirak.cbs.object.ChangePasswordReq;
import com.chirak.cbs.object.ForgotPasswordReq;
import com.chirak.cbs.repository.StudentRepository;
import com.chirak.cbs.security.SecurityService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;

@Service
@AllArgsConstructor
public class StudentService {
    private final AffiliateService affiliateService;
    private final StudentRepository studentRepo;
    private final StudentMapper studentMapper = StudentMapper.instance;
    private final EmailService emailService;
    private final TokenService tokenService;
    private final SecurityService securityService;


    @Transactional
    public void register(StudentDto studentDto, MultipartFile consentLetter) throws IOException, AffiliateNotFoundException, MessagingException {
        studentDto.setParentConsentLetter(consentLetter.getBytes());
        Student student = studentMapper.toStudent(studentDto);
        student.setPassword(studentDto.getPassword());
        if (studentDto.getAffiliateId() != null) {
            student.setAffiliate(affiliateService.findById(studentDto.getAffiliateId()));
        }
        Student persistedStudent = studentRepo.save(student);
        //send email confirmation mail
        emailService.sendEmailConfirmationMail(persistedStudent.getFirstName(), persistedStudent.getLastName(), persistedStudent.getEmail());
    }

    /**
     * Enables student after email confirmation.
     * @param token
     * @throws TokenException
     * @throws StudentNotFoundException
     */
    public void markAsEnabled(String token) throws TokenException, StudentNotFoundException {
        tokenService.validate(token);
        Student student = studentRepo.findByEmail(token.substring(7)).orElseThrow(StudentService::returnException);
        student.setEnabled(true);
        studentRepo.save(student);
    }


    public void markAsRegistered() throws StudentNotFoundException, AffiliateNotFoundException {
        Student student = securityService.authorizedStudent();
        student.setRegistered(true);
        student.setRegNumber("CBS-" + LocalDate.now().getYear() + "-" + student.getId());
        Student savedStudent = studentRepo.save(student);
        //increase balance of Affiliate and persist
        if (savedStudent.getAffiliate() != null) {
            Affiliate affiliate = savedStudent.getAffiliate();
            affiliate.increaseBalance(500);
            affiliateService.afterBalanceIncrease(affiliate);
        }
    }

    public void requestForgotPasswordMail(String email) throws StudentNotFoundException, MessagingException {
        Student student = studentRepo
                                            .findByEmail(email)
                                            .orElseThrow(StudentService::returnException);

        emailService.sendForgotPasswordMail(student.getFirstName(), student.getLastName(), email);
    }

    /**
     * Validates generated token.
     * @param token
     */
    public void validatePasswordResetToken(String token) throws TokenException {
        tokenService.validate(token);
    }

    public void resetPassword(ForgotPasswordReq resetReq) throws StudentNotFoundException {
        Student student = studentRepo
                                                .findByEmail(resetReq.getEmail())
                                                .orElseThrow(StudentService::returnException);
        student.setPassword(resetReq.getPassword());
        studentRepo.save(student);
    }

    /**
     * Returns authenticated student.
     * @return
     */
    public StudentDto get() throws StudentNotFoundException {
        Student student = securityService.authorizedStudent();
        StudentDto studentDto = studentMapper.toDto(student);
        studentDto.setPassword(null);
        if (student.getAffiliate() != null) {
            studentDto.setAffiliateId(student.getAffiliate().getAffiliateId());
        }

        return studentDto;
    }

    public UpdatedStudentDto update(UpdatedStudentDto dto, MultipartFile file) throws IOException {
        dto.setParentConsentLetter(file.getBytes()); //
        var student = studentMapper.toStudent(dto);
        student.setId(securityService.authorizedStudent().getId());
        student.setPassword(securityService.authorizedStudent().getPassword());
        student.setEnabled(securityService.authorizedStudent().isEnabled());
        student.setAffiliate(securityService.authorizedStudent().getAffiliate());

        var savedStudent = studentRepo.save(student);
        //map back to dto
        var updatedDto = studentMapper.toUpdatedStudentDto(savedStudent);
        if (savedStudent.getAffiliate() != null) {
            updatedDto.setAffiliateId(savedStudent.getAffiliate().getAffiliateId());
        }

        return updatedDto;
    }

    /**
     * Permits password change when user is logged in.
     * @param req
     * @throws PasswordException
     */
    public void newPassword(ChangePasswordReq req) throws PasswordException {
        var authStudent = securityService.authorizedStudent();
        authStudent.setPassword(req.getNewPassword());
        studentRepo.save(authStudent);
    }

    public void delete(HttpServletResponse response) {
        var student = securityService.authorizedStudent();
        studentRepo.deleteById(student.getId());
        //remove authentication
        securityService.removeAuthentication(response);
    }

    /**
     * Supplies {@link StudentNotFoundException}.
     * @return
     */
    private static StudentNotFoundException returnException() {
        return new StudentNotFoundException("Invalid email.");
    }
}