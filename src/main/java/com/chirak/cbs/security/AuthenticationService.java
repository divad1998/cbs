package com.chirak.cbs.security;

import com.chirak.cbs.entity.Student;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthenticationService {

    private final JwtService jwtService;
    private final AuthenticationManager authManager;

    /**
     * Authenticates students post login request.
     * @param authRequest
     * @param response
     */
    public void authenticate(AuthenticationRequest authRequest, HttpServletResponse response) {
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword());
        Authentication authResult = authManager.authenticate(authToken);

        Student student = (Student) authResult.getPrincipal();
        String jwt = jwtService.generateToken(student.getEmail());

        response.addHeader("Authorization", "Bearer " + jwt);
    }

}
