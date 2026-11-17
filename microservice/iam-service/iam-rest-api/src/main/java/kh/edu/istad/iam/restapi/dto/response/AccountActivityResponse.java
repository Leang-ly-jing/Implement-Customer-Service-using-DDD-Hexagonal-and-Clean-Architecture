package kh.edu.istad.iam.restapi.dto.response;

import java.time.Instant;

public record AccountActivityResponse(
        Long id,
        String action,
        String metadata,
        String ipAddress,
        String device,
        Instant createdAt
) {
}
