package kh.edu.istad.iam.domain.dto.command.user;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;
public record AssignRolesToUserCommand(
        UUID userUuid,
        Set<UUID> roleUuids,
        Instant expiresAt,
        String reason
) {}
