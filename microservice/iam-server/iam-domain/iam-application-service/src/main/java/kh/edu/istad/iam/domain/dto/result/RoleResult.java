package kh.edu.istad.iam.domain.dto.result;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record RoleResult(
        UUID uuid,
        String name,
        String description,
        boolean isSystem,
        boolean isActive,
        List<PermissionResult> permissions,
        Instant createdAt,
        Instant updatedAt
) {}