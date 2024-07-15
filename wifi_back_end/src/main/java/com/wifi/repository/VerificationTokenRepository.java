package com.wifi.repository;

import com.wifi.entity.VerificationToken;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {
    VerificationToken findByToken(String token);

    @Transactional
    int deleteAllByExpiryDateBefore(LocalDateTime now);
}
