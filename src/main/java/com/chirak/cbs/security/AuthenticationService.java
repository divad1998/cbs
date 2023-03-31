package com.chirak.cbs.security;

import com.chirak.cbs.entity.Affiliate;
import com.chirak.cbs.entity.Student;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthenticationService {

    private final JwtService jwtService;
    private final ProviderManager authManager;

    /**
     * Authenticates students post login request.
     * @param authRequest
     * @param response
     */
    public void authenticateStudent(AuthenticationRequest authRequest, HttpServletResponse response) {
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword());
        Authentication authResult = authManager.getProviders().get(1).authenticate(authToken);

        Student student = (Student) authResult.getPrincipal();
        String jwt = jwtService.generateToken(student.getEmail());

        response.addHeader("Authorization", "Bearer " + jwt);
    }

    public void authenticateAffiliate(AuthenticationRequest authRequest, HttpServletResponse response) {
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword());
        Authentication authResult = authManager.getProviders().get(0).authenticate(authToken); //the problem dey inside here: the manager call

        var affiliate = (Affiliate) authResult.getPrincipal(); //Solution 1: try and catch
        String jwt = jwtService.generateToken(affiliate.getEmail());

        response.addHeader("Authorization", "Bearer " + jwt);
    }
}