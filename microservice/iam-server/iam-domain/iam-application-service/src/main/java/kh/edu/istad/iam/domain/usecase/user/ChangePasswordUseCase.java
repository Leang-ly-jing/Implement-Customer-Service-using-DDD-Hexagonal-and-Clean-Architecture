package kh.edu.istad.iam.domain.usecase.user;

import kh.edu.istad.iam.domain.dto.command.user.ChangePasswordCommand;
import kh.edu.istad.iam.domain.entity.User;
import kh.edu.istad.iam.domain.exception.AuthorizationDomainException;
import kh.edu.istad.iam.domain.port.out.CurrentActorProvider;
import kh.edu.istad.iam.domain.port.out.PasswordHasher;
import kh.edu.istad.iam.domain.port.out.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
@RequiredArgsConstructor
public class ChangePasswordUseCase {

    private final CurrentActorProvider currentActorProvider;
    private final UserRepository userRepository;
    private final PasswordHasher passwordHasher;

    public void execute(ChangePasswordCommand command) {
        User user = userRepository.findByUuid(command.userUuid())
                .orElseThrow(() -> new AuthorizationDomainException("User not found"));

        if (!command.newPassword().equals(command.confirmPassword())) {
            throw new AuthorizationDomainException("Passwords do not match");
        }

        if (user.getPasswordHash() != null && !user.getPasswordHash().isBlank()) {
            if (command.currentPassword() == null || !passwordHasher.matches(command.currentPassword(), user.getPasswordHash())) {
                throw new AuthorizationDomainException("Current password is incorrect");
            }
        }

        user.setPassword(passwordHasher.hash(command.newPassword()));
        user.setPasswordChangedAt(Instant.now());
        user.setUpdatedAt(Instant.now());
        user.setUpdatedBy(currentActorProvider.getCurrentActor());

        userRepository.save(user);
    }
}
