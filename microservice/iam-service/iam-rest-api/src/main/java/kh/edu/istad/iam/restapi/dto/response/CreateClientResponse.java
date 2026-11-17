package kh.edu.istad.iam.restapi.dto.response;

import java.time.Instant;
import java.util.Set;

public record CreateClientResponse(
        String id,
        String clientId,
        String clientName,
        String rawSecret,
        Instant clientIdIssuedAt,
        Set<String> clientAuthenticationMethods,
        Set<String> authorizationGrantTypes,
        Set<String> redirectUris,
        Set<String> scopes
) {}
