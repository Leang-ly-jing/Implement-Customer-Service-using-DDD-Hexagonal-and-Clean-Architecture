package kh.edu.istad.iam.domain.mapper;


import kh.edu.istad.iam.domain.dto.command.identity.FederatedIdentityCommand;
import kh.edu.istad.iam.domain.dto.result.IdentityResult;
import kh.edu.istad.iam.domain.entity.Identity;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.UUID;
@Component
public class IdentityDataMapper {

    public Identity federatedCommandToIdentity(Long userId, FederatedIdentityCommand command, String actor, Instant now) {
        return Identity.builder()
                .uuid(UUID.randomUUID())
                .userId(userId)
                .provider(command.provider())
                .providerSubject(command.providerSubject())
                .email(command.email())
                .emailVerified(command.emailVerified())
                .displayName(command.displayName())
                .username(command.username())
                .pictureUrl(command.pictureUrl())
                .attributes(command.attributesJson())
                .linkedAt(now)
                .lastLoginAt(now)
                .deleted(false)
                .createdAt(now)
                .createdBy(actor)
                .updatedAt(now)
                .updatedBy(actor)
                .version(0L)
                .build();
    }

    public IdentityResult identityToIdentityResult(Identity identity) {
        return new IdentityResult(
                identity.getUuid(),
                identity.getProvider() != null ? identity.getProvider().name() : null,
                identity.getEmail(),
                identity.getDisplayName(),
                identity.getLinkedAt()
        );
    }
}
