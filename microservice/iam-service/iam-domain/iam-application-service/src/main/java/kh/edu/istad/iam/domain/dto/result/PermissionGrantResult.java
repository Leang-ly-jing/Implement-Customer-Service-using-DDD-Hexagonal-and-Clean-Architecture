package kh.edu.istad.iam.domain.dto.result;

import java.time.Instant;
import java.util.UUID;

public record PermissionGrantResult(
        UUID permissionUuid,
        String permissionName,
        String effect,
        Instant assignedAt,
        Instant expiresAt,
        String reason
) {}