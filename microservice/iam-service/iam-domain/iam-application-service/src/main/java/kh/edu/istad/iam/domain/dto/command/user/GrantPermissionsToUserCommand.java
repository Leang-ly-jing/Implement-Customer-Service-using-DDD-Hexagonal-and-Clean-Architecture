package kh.edu.istad.iam.domain.dto.command.user;

import kh.edu.istad.iam.domain.valueobject.PermissionEffect;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

public record GrantPermissionsToUserCommand(
        UUID userUuid,
        Set<UUID> permissionUuids,
        PermissionEffect effect,
        Instant expiresAt,
        String reason
) {}