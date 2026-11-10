package kh.edu.istad.iam.domain.dto.command.role;

import java.util.UUID;

public record UpdateRoleCommand(
        UUID roleUuid,
        String description
) {}