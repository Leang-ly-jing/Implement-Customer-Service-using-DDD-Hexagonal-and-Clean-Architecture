package kh.edu.istad.iam.restapi.service;

import kh.edu.istad.iam.domain.dto.result.SystemConfigResult;
import kh.edu.istad.iam.domain.valueobject.AuditAction;
import kh.edu.istad.iam.persistence.repository.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoginAttemptService {

    private final UserJpaRepository userRepository;
    private final SystemConfigService configService;
    private final AuditLogService audit;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void onFailure(String username) {
        if (username == null || username.isBlank()) {
            return;
        }

        userRepository.findByUsernameOrEmail(username.trim()).ifPresent(u -> {
            SystemConfigResult c = configService.get();
            int attempts = u.getFailedLoginAttempts() + 1;
            u.setFailedLoginAttempts(attempts);

            if (attempts >= c.maxFailedAttempts()) {
                Instant lockUntil = Instant.now().plus(Duration.ofMinutes(c.lockDurationMinutes()));
                u.setLockedUntil(lockUntil);
                u.setFailedLoginAttempts(0);
                audit.record(
                        "system",
                        AuditAction.ACCOUNT_LOCKED,
                        "USER",
                        u.getUuid().toString(),
                        "Account automatically locked until " + lockUntil + " after " + attempts + " failed attempts",
                        null
                );
                log.warn("User {} locked until {} due to {} failed login attempts", u.getUsername(), lockUntil, attempts);
            }
            userRepository.save(u);
        });
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void onSuccess(String username) {
        if (username == null || username.isBlank()) {
            return;
        }

        userRepository.findByUsernameOrEmail(username.trim()).ifPresent(u -> {
            if (u.getFailedLoginAttempts() > 0 || u.getLockedUntil() != null) {
                u.setFailedLoginAttempts(0);
                u.setLockedUntil(null);
                userRepository.save(u);
            }
        });
    }
}
