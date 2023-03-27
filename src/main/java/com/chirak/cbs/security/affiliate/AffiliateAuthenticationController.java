package com.chirak.cbs.security.affiliate;

import com.chirak.cbs.security.AuthenticationRequest;
import com.chirak.cbs.security.AuthenticationService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/affiliates/auth") //NOt the reason
@AllArgsConstructor
public class AffiliateAuthenticationController {
    private final AuthenticationService authService;

    @PostMapping
    public void authenticate(@Valid @RequestBody AuthenticationRequest authRequest, HttpServletResponse response) {
        authService.authenticateAffiliate(authRequest, response);
    }
}
