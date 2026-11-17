package kh.edu.istad.iam.restapi.dto.response;

import java.time.Instant;
import java.util.UUID;

public record LoginHistoryResponse(
        Long id,
        UUID userUuid,
        String username,
        boolean success,
        String status,
        String failureReason,
        String ipAddress,
        String device,
        String deviceType,
        boolean suspicious,
        String suspiciousReason,
        Instant loginAt
) {}
