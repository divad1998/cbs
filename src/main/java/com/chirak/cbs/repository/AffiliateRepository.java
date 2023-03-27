package com.chirak.cbs.repository;

import com.chirak.cbs.entity.Affiliate;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface AffiliateRepository extends CrudRepository<Affiliate, String> {
    Optional<Affiliate> findByReferralCode(String referralCode);

    Optional<Affiliate> findByEmail(String email);
}
