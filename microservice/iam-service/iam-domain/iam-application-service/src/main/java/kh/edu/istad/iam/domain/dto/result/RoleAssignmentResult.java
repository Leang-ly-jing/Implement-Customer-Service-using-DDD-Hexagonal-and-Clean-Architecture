package kh.edu.istad.iam.domain.dto.result;

import java.time.Instant;
import java.util.UUID;

public record RoleAssignmentResult(
        UUID roleUuid,
        String roleName,
        Instant assignedAt,
        Instant expiresAt,
        String reason
) {}