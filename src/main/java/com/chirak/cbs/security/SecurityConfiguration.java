package com.chirak.cbs.security;

import com.chirak.cbs.role.Role;
import lombok.AllArgsConstructor;
import org.apache.catalina.User;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfiguration {
    private final AuthenticationProvider authenticationProvider;
    private final StudentJwtAuthorizationFilter studentJwtAuthorizationFilter;

    @Bean
    public SecurityFilterChain studentFilterChain(HttpSecurity http) throws Exception {
        return http
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
                        .authenticationProvider(authenticationProvider)
                        .addFilterBefore(studentJwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class)
                        .build();
    }

//    @Bean
//    public SecurityFilterChain affiliateFilterChain(HttpSecurity http) {
//        return http
//                        .securityMatcher("/api/affiliates", "/api/affiliates/**") //will I need to deactivate from login and...?
//                        .authorizeHttpRequests()
//                        .requestMatchers();
//    }
}