package com.chirak.cbs.repository;

import com.chirak.cbs.object.Token;
import org.springframework.data.repository.CrudRepository;

public interface TokenRepository extends CrudRepository<Token, Long> {
    Token findByToken(String token);
}