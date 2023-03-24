package com.chirak.cbs.service;

import com.chirak.cbs.entity.Affiliate;
import com.chirak.cbs.exception.AffiliateNotFoundException;
import com.chirak.cbs.repository.AffiliateRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AffiliateService {
    public final AffiliateRepository affiliateRepo;

    public Affiliate findById(String affiliateId) throws AffiliateNotFoundException {
        if (affiliateRepo.findById(affiliateId).isPresent()) {
            return affiliateRepo.findById(affiliateId).get();
        } else {
            throw new AffiliateNotFoundException("Invalid Affiliate.");
        }
    }

    public void afterBalanceIncrease(Affiliate affiliate) {
        affiliateRepo.save(affiliate);
    }
}
