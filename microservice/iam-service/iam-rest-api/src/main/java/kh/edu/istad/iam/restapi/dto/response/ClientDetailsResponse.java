package kh.edu.istad.iam.restapi.dto.response;

import java.time.Instant;
import java.util.Set;

public record ClientDetailsResponse(
        String id,
        String clientId,
        String clientName,
        Instant clientIdIssuedAt,
        Instant clientSecretExpiresAt,
        boolean hasSecret,
        Set<String> clientAuthenticationMethods,
        Set<String> authorizationGrantTypes,
        Set<String> redirectUris,
        Set<String> postLogoutRedirectUris,
        Set<String> scopes,
        boolean requireProofKey,
        boolean requireAuthorizationConsent,
        long accessTokenTimeToLiveMinutes,
        long refreshTokenTimeToLiveDays
) {}
