package kh.edu.istad.iam.domain.dto.command.role;

import java.util.Set;
import java.util.UUID;

public record CreateRoleCommand(
        String name,
        String description,
        Set<UUID> permissionUuids
) {}