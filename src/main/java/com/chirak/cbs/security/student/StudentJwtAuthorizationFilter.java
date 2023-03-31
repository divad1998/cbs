package com.chirak.cbs.security.student;


import com.chirak.cbs.entity.Student;
import com.chirak.cbs.security.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@AllArgsConstructor
public class StudentJwtAuthorizationFilter extends OncePerRequestFilter {
    private final StudentDetailsService detailsService;
    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authValue = request.getHeader("Authorization");
        if (authValue != null && authValue.startsWith("Bearer ")) {
            String token = authValue.substring(7);
            Student student = detailsService.loadUserByUsername(jwtService.extractUsername(token));
            if (!jwtService.isExpired(token)) {
                UsernamePasswordAuthenticationToken auth =  new UsernamePasswordAuthenticationToken(student, null, student.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(auth);

               response.setHeader("Authorization", request.getHeader("Authorization")); //Check whether this is
            }
        }

        filterChain.doFilter(request, response);
    }
}