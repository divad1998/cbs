package com.chirak.cbs.security;

import com.chirak.cbs.role.Role;
import com.chirak.cbs.security.affiliate.AffiliateDetailsService;
import com.chirak.cbs.security.affiliate.AffiliateJwtAuthorizationFilter;
import com.chirak.cbs.security.student.StudentDetailsService;
import com.chirak.cbs.security.student.StudentJwtAuthorizationFilter;
import jakarta.servlet.Filter;
import lombok.AllArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
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
    private final AffiliateJwtAuthorizationFilter affiliateJwtAuthorizationFilter;
    private final StudentDetailsService studentDetailsService;
    private final AffiliateDetailsService affiliateDetailsService;

    @Bean
    public SecurityFilterChain affiliateFilterChain(HttpSecurity http) throws Exception {
        return http
                        .securityMatcher("/api/affiliates", "/api/affiliates/**")
                        .cors().disable()
                        .csrf().disable()
                        .formLogin().disable()
                        .httpBasic().disable()
                        .authorizeHttpRequests()
                        .requestMatchers(HttpMethod.POST, "/api/affiliates").permitAll()
                        .requestMatchers("/api/affiliates/confirm", "/api/affiliates/auth", "/api/affiliates/forgot-password").permitAll()
                        .requestMatchers("/api/affiliates/confirm/**").permitAll()
                        .requestMatchers("/api/affiliates", "/api/affiliates/referrals", "/api/affiliates/balance", "/api/affiliates/balance/deduction").hasRole(Role.AFFILIATE.name())
                        .requestMatchers("/api/affiliates/new-email", "/api/affiliates/new-password", "/api/affiliates/logout").hasRole(Role.AFFILIATE.name())
                        .anyRequest()
                            .authenticated()
                        .and()
                        .addFilterBefore(affiliateJwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class)
                        .sessionManagement()
                            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                        .and()
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
                        .requestMatchers("/api/students/auth", "/api/students/forgot-password").permitAll()
                        .requestMatchers("/api/students/confirm", "/api/students/confirm/**").permitAll()
                        .requestMatchers("/api/students/complete-registration").hasRole(Role.STUDENT.name())
                        .requestMatchers(HttpMethod.GET, "/api/students", "/api/students/logout").hasRole(Role.STUDENT.name())
                        .requestMatchers(HttpMethod.PUT, "/api/students").hasRole(Role.STUDENT.name())
                        .requestMatchers(HttpMethod.PATCH, "/api/students/new-password").hasRole(Role.STUDENT.name())
                        .requestMatchers(HttpMethod.DELETE, "/api/students").hasRole(Role.STUDENT.name())
                        .requestMatchers(HttpMethod.POST, "/api/students/new-email").hasRole(Role.STUDENT.name())
                        .anyRequest().authenticated()
                        .and()
                        .addFilterBefore(studentJwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class)
                        .sessionManagement()
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                        .and()
                        .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider affiliateAuthenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(affiliateDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationProvider studentAuthenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(studentDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }


    @Bean
    public ProviderManager authenticationManager(List<AuthenticationProvider> authenticationProviders) {
        return new ProviderManager(authenticationProviders);
    }

    /**
     * Configures {@link AffiliateJwtAuthorizationFilter} bean.
     * @param filter filter bean to be configured.
     * @return
     */
    @Bean(name = "ConfiguredAffiliateJwtAuthorizationFilter")
    public FilterRegistrationBean<? extends Filter> configureFilter1(AffiliateJwtAuthorizationFilter filter) {
        return disableFilter(filter);
    }

    /**
     * Configures {@link StudentJwtAuthorizationFilter} bean.
     * @param filter filter bean to be configured.
     * @return
     */
    @Bean(name = "ConfiguredStudentJwtAuthorizationFilter")
    public FilterRegistrationBean<? extends Filter> configureFilter2(StudentJwtAuthorizationFilter filter) {
        return disableFilter(filter);
    }

    /**
     * Configures a filter by disabling it, so it isn't automatically added to a security filterChain.
     * @param filter filter to be disabled
     * @return configured filter
     */
    private FilterRegistrationBean<? extends Filter> disableFilter(Filter filter) {
        FilterRegistrationBean<? extends Filter> registeredBean = new FilterRegistrationBean<>(filter);
        registeredBean.setEnabled(false);
        return registeredBean;
    }
}