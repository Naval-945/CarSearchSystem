package com.wifi.utils;

import com.wifi.repository.VerificationTokenRepository;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import org.slf4j.Logger;


@Component
public class VerificationTokenCleanupTask {

    private static final Logger logger = LoggerFactory.getLogger(VerificationTokenCleanupTask.class);

    @Autowired
    private VerificationTokenRepository tokenRepository;

    @Scheduled(fixedRate = 20000) // 每 20 秒执行一次
    public void cleanupExpiredTokens() {
        LocalDateTime now = LocalDateTime.now();
        int deletedCount = tokenRepository.deleteAllByExpiryDateBefore(now);
        System.out.println("Deleted " + deletedCount + " expired tokens at " + now);
        logger.info("Deleted {} expired tokens at {}", deletedCount, now);
    }
}


