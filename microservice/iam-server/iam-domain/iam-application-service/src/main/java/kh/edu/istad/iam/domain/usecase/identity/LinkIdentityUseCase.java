package kh.edu.istad.iam.domain.usecase.identity;

import kh.edu.istad.iam.domain.dto.command.identity.FederatedIdentityCommand;
import kh.edu.istad.iam.domain.dto.result.IdentityResult;
import kh.edu.istad.iam.domain.entity.Identity;
import kh.edu.istad.iam.domain.entity.User;
import kh.edu.istad.iam.domain.exception.AuthorizationDomainException;
import kh.edu.istad.iam.domain.mapper.IdentityDataMapper;
import kh.edu.istad.iam.domain.port.out.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
@Component
@RequiredArgsConstructor
public class LinkIdentityUseCase {
    private final UserRepository userRepository;
    private final IdentityDataMapper identityDataMapper;

    public IdentityResult execute(UUID userUuid, FederatedIdentityCommand command) {
        User user = userRepository.findByUuid(userUuid)
                .orElseThrow(() ->
                        new AuthorizationDomainException("User not found"));

        List<Identity> existing = userRepository.findIdentitiesByUserId(user.getId());

        Identity newIdentity = identityDataMapper.federatedCommandToIdentity
                (user.getId(), command, user.getUsernameValue(), Instant.now());

        user.validateCanLinkIdentity(existing, newIdentity);
        Identity saved = userRepository.saveIdentity(newIdentity);

        return identityDataMapper.identityToIdentityResult(saved);
    }
}
