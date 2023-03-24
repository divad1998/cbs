package com.chirak.cbs.security;

import com.chirak.cbs.entity.Student;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class SecurityService {

    /**
     * Returns authorized student.
     *
     * @return
     */
    public Student authorizedStudent() {
        return (Student) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    public void removeAuthentication(HttpServletResponse response) {
        response.reset(); //try this.

    }
}
