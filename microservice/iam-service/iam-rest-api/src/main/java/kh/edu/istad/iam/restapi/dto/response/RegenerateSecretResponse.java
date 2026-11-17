package kh.edu.istad.iam.restapi.dto.response;

import java.time.Instant;

public record RegenerateSecretResponse(
        String clientId,
        String rawSecret,
        Instant issuedAt
) {}
