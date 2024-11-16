package com.example.userservice.repository;

import com.example.userservice.model.TokenBlackList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TokenBlacklistRepository extends JpaRepository<TokenBlackList, Integer> {
    Optional<TokenBlackList> findByToken(String token);
}
