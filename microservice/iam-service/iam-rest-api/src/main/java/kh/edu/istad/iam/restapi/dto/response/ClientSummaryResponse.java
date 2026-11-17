package kh.edu.istad.iam.restapi.dto.response;

import java.time.Instant;
import java.util.Set;

public record ClientSummaryResponse(
        String id,
        String clientId,
        String clientName,
        Instant clientIdIssuedAt,
        Set<String> clientAuthenticationMethods,
        Set<String> authorizationGrantTypes,
        Set<String> scopes,
        int redirectUrisCount,
        boolean requireProofKey,
        boolean requireAuthorizationConsent
) {}
