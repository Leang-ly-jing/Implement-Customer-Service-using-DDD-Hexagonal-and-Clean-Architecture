package kh.edu.istad.iam.restapi.dto.response;

import java.time.Instant;
import java.util.UUID;

public record UserSessionResponse(
        UUID id,
        String deviceType,
        String browser,
        String ipAddress,
        String location,
        Instant lastActiveAt,
        Instant createdAt,
        boolean current
) {}