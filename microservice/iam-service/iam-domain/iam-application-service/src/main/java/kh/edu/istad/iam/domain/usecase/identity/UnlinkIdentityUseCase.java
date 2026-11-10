package kh.edu.istad.iam.domain.usecase.identity;

import kh.edu.istad.iam.domain.entity.Identity;
import kh.edu.istad.iam.domain.entity.User;
import kh.edu.istad.iam.domain.exception.AuthorizationDomainException;
import kh.edu.istad.iam.domain.port.out.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class UnlinkIdentityUseCase {

    private final UserRepository userRepository;

    public void execute(UUID userUuid, UUID identityUuid) {
        User user = userRepository.findByUuid(userUuid)
                .orElseThrow(() ->
                        new AuthorizationDomainException("User not found"));

        Identity identity = userRepository.findIdentityByUuid(identityUuid)
                .orElseThrow(() -> new AuthorizationDomainException("Identity not found"));

        List<Identity> existing = userRepository.findIdentitiesByUserId(user.getId());
        user.validateCanUnlinkIdentity(existing, identity);

        identity.setDeleted(true);
        identity.setDeletedAt(Instant.now());
        identity.setDeletedBy(user.getUsernameValue());

        userRepository.saveIdentity(identity);
    }
}
