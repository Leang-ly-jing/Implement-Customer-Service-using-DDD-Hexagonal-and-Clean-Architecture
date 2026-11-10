package kh.edu.istad.iam.domain.dto.command.role;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

public record AssignPermissionsToRoleCommand(
        UUID roleUuid,
        Set<UUID> permissionUuids,
        Instant expiresAt,
        String reason
) {}