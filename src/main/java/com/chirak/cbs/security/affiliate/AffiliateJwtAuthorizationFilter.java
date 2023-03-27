package com.chirak.cbs.security.affiliate;

import com.chirak.cbs.entity.Affiliate;
import com.chirak.cbs.entity.Student;
import com.chirak.cbs.security.JwtService;
import com.chirak.cbs.security.student.StudentDetailsService;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@AllArgsConstructor
public class AffiliateJwtAuthorizationFilter extends OncePerRequestFilter {
    private final AffiliateDetailsService detailsService;
    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException
    {
        String authValue = request.getHeader("Authorization");
        if (authValue != null && authValue.startsWith("Bearer ")) {
            String token = authValue.substring(7);
            Affiliate affiliate = detailsService.loadUserByUsername(jwtService.extractUsername(token));
            if (!jwtService.isExpired(token)) {
                UsernamePasswordAuthenticationToken auth =  new UsernamePasswordAuthenticationToken(affiliate, null, affiliate.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(auth);

                response.setHeader("Authorization", request.getHeader("Authorization"));
            }
        }

        filterChain.doFilter(request, response);
    }
}