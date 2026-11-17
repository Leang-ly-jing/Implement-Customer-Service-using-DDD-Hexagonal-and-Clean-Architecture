package kh.edu.istad.iam.restapi.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.Set;

public record CreateClientRequest(
        @NotBlank(message = "Client ID is required")
        @Size(max = 100)
        String clientId,

        @NotBlank(message = "Client name is required")
        @Size(max = 200)
        String clientName,

        @Size(max = 200)
        String clientSecret,

        @NotEmpty(message = "At least one authentication method must be selected")
        Set<String> clientAuthenticationMethods,

        @NotEmpty(message = "At least one authorization grant type must be selected")
        Set<String> authorizationGrantTypes,

        Set<String> redirectUris,

        Set<String> postLogoutRedirectUris,

        @NotEmpty(message = "At least one scope must be selected")
        Set<String> scopes,

        Boolean requireProofKey,

        Boolean requireAuthorizationConsent,

        Long accessTokenTimeToLiveMinutes,

        Long refreshTokenTimeToLiveDays
) {}
