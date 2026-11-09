package kh.edu.istad.iam.domain.dto.command.permission;

public record CreatePermissionCommand(
        String resource,
        String action,
        String description
) {}