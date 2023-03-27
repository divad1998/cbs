package com.chirak.cbs.security.affiliate;

import com.chirak.cbs.entity.Affiliate;
import com.chirak.cbs.repository.AffiliateRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AffiliateDetailsService implements UserDetailsService {
    private final AffiliateRepository repo;

    @Override
    public Affiliate loadUserByUsername(String username) throws UsernameNotFoundException {
        return repo.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("Invalid email."));
    }
}
