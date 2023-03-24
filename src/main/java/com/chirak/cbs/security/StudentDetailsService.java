package com.chirak.cbs.security;

import com.chirak.cbs.entity.Student;
import com.chirak.cbs.repository.StudentRepository;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class StudentDetailsService implements UserDetailsService {
    private final StudentRepository studentRepo;

    @Override
    public Student loadUserByUsername(String username) throws UsernameNotFoundException {
        return studentRepo
                            .findByEmail(username)
                            .orElseThrow(() ->  new UsernameNotFoundException("Invalid email."));
    }
}
