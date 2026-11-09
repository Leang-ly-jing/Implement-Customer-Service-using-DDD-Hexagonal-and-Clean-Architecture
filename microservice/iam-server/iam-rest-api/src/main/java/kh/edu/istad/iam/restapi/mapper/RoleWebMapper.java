package kh.edu.istad.iam.restapi.mapper;

import kh.edu.istad.iam.domain.dto.command.role.AssignPermissionsToRoleCommand;
import kh.edu.istad.iam.domain.dto.command.role.CreateRoleCommand;
import kh.edu.istad.iam.domain.dto.command.role.UpdateRoleCommand;
import kh.edu.istad.iam.domain.dto.result.RoleResult;
import kh.edu.istad.iam.restapi.dto.request.AssignPermissionsToRoleRequest;
import kh.edu.istad.iam.restapi.dto.request.CreateRoleRequest;
import kh.edu.istad.iam.restapi.dto.request.UpdateRoleRequest;
import kh.edu.istad.iam.restapi.dto.response.PermissionResponse;
import kh.edu.istad.iam.restapi.dto.response.RoleResponse;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Component
public class RoleWebMapper {

    public CreateRoleCommand toCommand(CreateRoleRequest request) {
        Set<UUID> permissionUuids = request.permissionUuids() != null
                ? new HashSet<>(request.permissionUuids())
                : Set.of();

        return new CreateRoleCommand(
                request.name(),
                request.description(),
                permissionUuids
        );
    }

    public UpdateRoleCommand toCommand(UUID uuid, UpdateRoleRequest request) {
        return new UpdateRoleCommand(
                uuid,
                request.description()
        );
    }

    public AssignPermissionsToRoleCommand toCommand(UUID uuid, AssignPermissionsToRoleRequest request) {
        Set<UUID> permissionUuids = request.permissionUuids() != null
                ? new HashSet<>(request.permissionUuids())
                : Set.of();

        return new AssignPermissionsToRoleCommand(
                uuid,
                permissionUuids,
                null,
                request.reason()
        );
    }

    public RoleResponse toResponse(RoleResult result) {
        List<PermissionResponse> permissions = List.of();
        if (result.permissions() != null) {
            permissions = result.permissions().stream()
                    .map(p -> new PermissionResponse(
                            p.uuid(),
                            p.name(),
                            p.resource(),
                            p.action(),
                            p.description(),
                            p.isSystem(),
                            p.isActive(),
                            p.createdAt(),
                            p.updatedAt()
                    ))
                    .toList();
        }

        return new RoleResponse(
                result.uuid(),
                result.name(),
                result.description(),
                result.isSystem(),
                result.isActive(),
                permissions,
                result.createdAt(),
                result.updatedAt()
        );
    }
}
