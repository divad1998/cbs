package com.chirak.cbs.security;

import com.chirak.cbs.entity.Affiliate;
import com.chirak.cbs.entity.Student;
import com.chirak.cbs.security.affiliate.AffiliateAuthenticationController;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class SecurityService {

    /**
     * Returns authenticated student.
     *
     * @return
     */
    public Student authenticatedStudent() {
        return (Student) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    /**
     * Returns authenticated affiliate
     * @return
     */
    public Affiliate authenticatedAffiliate() {
        return (Affiliate) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    public void removeAuthentication(HttpServletResponse response) {
        response.reset();
    }

}
