package kh.edu.istad.iam.domain.dto.command.permission;

import java.util.UUID;

public record UpdatePermissionCommand(
        UUID permissionUuid,
        String description
) {}