package kh.edu.istad.iam.restapi.dto.request;

import jakarta.validation.constraints.NotEmpty;
import java.util.List;
import java.util.UUID;
public record AssignPermissionsToRoleRequest(
        @NotEmpty
        List<UUID> permissionUuids,
        String reason
) {}