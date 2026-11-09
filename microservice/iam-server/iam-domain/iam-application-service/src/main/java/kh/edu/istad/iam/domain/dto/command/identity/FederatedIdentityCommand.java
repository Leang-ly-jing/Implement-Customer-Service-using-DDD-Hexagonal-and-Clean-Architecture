package kh.edu.istad.iam.domain.dto.command.identity;

import kh.edu.istad.iam.domain.valueobject.IdentityProvider;

public record FederatedIdentityCommand(
        IdentityProvider provider,
        String providerSubject,
        String email,
        boolean emailVerified,
        String displayName,
        String username,
        String pictureUrl,
        String attributesJson
) {}