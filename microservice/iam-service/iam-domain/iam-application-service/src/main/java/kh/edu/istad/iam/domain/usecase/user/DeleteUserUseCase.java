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
public class DeleteUserUseCase {

    private final UserRepository userRepository;
    private final CurrentActorProvider currentActorProvider;

    public void execute(UUID userUuid) {
        User user = userRepository.findByUuid(userUuid)
                .orElseThrow(() ->
                        new AuthorizationDomainException("User not found!"));

        user.setDeleted(true);
        user.setDeletedAt(Instant.now());
        user.setDeletedBy(currentActorProvider.getCurrentActor());
        userRepository.save(user);
    }
}