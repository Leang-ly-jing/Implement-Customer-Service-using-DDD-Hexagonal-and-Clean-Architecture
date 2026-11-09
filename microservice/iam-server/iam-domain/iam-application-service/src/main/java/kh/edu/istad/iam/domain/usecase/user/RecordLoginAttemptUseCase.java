package kh.edu.istad.iam.domain.usecase.user;

import kh.edu.istad.iam.domain.entity.User;
import kh.edu.istad.iam.domain.exception.AuthorizationDomainException;
import kh.edu.istad.iam.domain.port.out.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.time.Instant;
@Component
@RequiredArgsConstructor
public class RecordLoginAttemptUseCase {

    private final UserRepository userRepository;

    public void execute(String usernameOrEmail, boolean success) {
        User user = userRepository.findByUsernameOrEmail(usernameOrEmail)
                .orElseThrow(() -> new AuthorizationDomainException("User not found"));

        Instant now = Instant.now();
        if (success) {
            user.setLastLoginAt(now);
            user.setFailedLoginAttempts(0);
        } else {
            int attempts = user.getFailedLoginAttempts() + 1;
            user.setFailedLoginAttempts(attempts);
            if (attempts >= 5) {
                user.setAccountNonLocked(false);
            }
        }
        user.setUpdatedAt(now);
        userRepository.save(user);
    }
}