package com.chirak.cbs.security;

import com.chirak.cbs.role.Role;
import com.chirak.cbs.security.affiliate.AffiliateDetailsService;
import com.chirak.cbs.security.affiliate.AffiliateJwtAuthorizationFilter;
import com.chirak.cbs.security.student.StudentDetailsService;
import com.chirak.cbs.security.student.StudentJwtAuthorizationFilter;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.List;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfiguration {
    private final StudentJwtAuthorizationFilter studentJwtAuthorizationFilter;
    private final StudentDetailsService studentDetailsService;
    private final AffiliateDetailsService affiliateDetailsService;
    private final AffiliateJwtAuthorizationFilter affiliateJwtAuthorizationFilter;

    @Bean
    @Order(1)
    public SecurityFilterChain affiliateFilterChain(HttpSecurity http) throws Exception {
        return http
                        .securityMatcher("/api/affiliates", "/api/affiliates/**")
                        .cors().disable()
                        .csrf().disable()
                        .formLogin().disable()
                        .httpBasic().disable()
                        .authorizeHttpRequests()
                        .requestMatchers(HttpMethod.POST, "/api/affiliates").permitAll()
                        .requestMatchers("/api/affiliates/confirm", "/api/affiliates/auth", "/api/affiliates/forgot-password", "/api/affiliates/forgot-password/**").permitAll()
                        .requestMatchers("/api/affiliates", "/api/affiliates/referrals").hasRole(Role.AFFILIATE.name())
                        .anyRequest()
                            .authenticated()
                        .and()
                        .authenticationProvider(affiliateAuthenticationProvider())
                        .addFilterBefore(affiliateJwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class)
                        .build();
    }

    @Bean
    public SecurityFilterChain studentFilterChain(HttpSecurity http) throws Exception {
        return http
                        .securityMatcher("/api/students", "/api/students/**")
                        .cors().disable()
                        .csrf().disable()
                        .httpBasic().disable()
                        .formLogin().disable()
                        .authorizeHttpRequests()
                        .requestMatchers(HttpMethod.POST, "/api/students").permitAll()
                        .requestMatchers("/api/students/auth", "/api/students/forgot-password", "/api/students/forgot-password/**").permitAll()
                        .requestMatchers("/api/students/confirm").permitAll()
                        .requestMatchers("/api/students/complete-registration").hasRole(Role.STUDENT.name())
                        .requestMatchers(HttpMethod.GET, "/api/students").hasRole(Role.STUDENT.name())
                        .requestMatchers(HttpMethod.PUT, "/api/students").hasRole(Role.STUDENT.name())
                        .requestMatchers(HttpMethod.PATCH, "/api/students/new-password").hasRole(Role.STUDENT.name())
                        .requestMatchers(HttpMethod.DELETE, "/api/students").hasRole(Role.STUDENT.name())
                        .anyRequest().authenticated()
                        .and()
                        .authenticationProvider(studentAuthenticationProvider())
                        .addFilterBefore(studentJwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class)
                        .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider studentAuthenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(studentDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationProvider affiliateAuthenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(affiliateDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(List<AuthenticationProvider> authenticationProviders) throws Exception {
        return new ProviderManager(authenticationProviders);
    }
}