package kh.edu.istad.iam.restapi.dto.response;

import kh.edu.istad.iam.domain.valueobject.AuditAction;

import java.time.Instant;
import java.util.UUID;

public record AuditLogResponse(
        UUID id,
        String actor,
        AuditAction action,
        String targetType,
        String targetId,
        String details,
        String ipAddress,
        Instant createdAt
) {
}
