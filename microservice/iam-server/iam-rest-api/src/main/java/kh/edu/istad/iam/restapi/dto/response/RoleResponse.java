package kh.edu.istad.iam.restapi.dto.response;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record RoleResponse(
        UUID uuid,
        String name,
        String description,
        boolean system,
        boolean active,
        List<PermissionResponse> permissions,
        Instant createdAt,
        Instant updatedAt
) {}
