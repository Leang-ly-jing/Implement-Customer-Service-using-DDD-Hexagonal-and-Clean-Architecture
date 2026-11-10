package kh.edu.istad.iam.domain.dto.result;

import java.time.Instant;
import java.util.UUID;

public record PermissionResult(
        UUID uuid,
        String name,
        String resource,
        String action,
        String description,
        boolean isSystem,
        boolean isActive,
        Instant createdAt,
        Instant updatedAt
) {}
