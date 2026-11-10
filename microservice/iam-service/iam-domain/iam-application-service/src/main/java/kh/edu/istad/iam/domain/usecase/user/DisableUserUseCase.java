package kh.edu.istad.iam.domain.usecase.user;

import kh.edu.istad.iam.domain.entity.User;
import kh.edu.istad.iam.domain.exception.AuthorizationDomainException;
import kh.edu.istad.iam.domain.port.out.CurrentActorProvider;
import kh.edu.istad.iam.domain.port.out.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.UUID;
@Component
@RequiredArgsConstructor
public class DisableUserUseCase {

    private final CurrentActorProvider currentActorProvider;
    private final UserRepository userRepository;


    public void execute(UUID userUuid) {
        User user = userRepository.findByUuid(userUuid)
                .orElseThrow(() ->
                        new AuthorizationDomainException("User not found"));

        user.setEnabled(false);
        user.setUpdatedAt(Instant.now());
        user.setUpdatedBy(currentActorProvider.getCurrentActor());
        userRepository.save(user);
    }
}