package kh.edu.istad.iam.domain.mapper;

import kh.edu.istad.iam.domain.dto.command.role.CreateRoleCommand;
import kh.edu.istad.iam.domain.dto.result.PermissionResult;
import kh.edu.istad.iam.domain.dto.result.RoleResult;
import kh.edu.istad.iam.domain.dto.result.RoleSummary;
import kh.edu.istad.iam.domain.entity.Role;
import org.springframework.stereotype.Component;

import java.time.Instant;

import java.util.List;
import java.util.UUID;
@Component
public class RoleDataMapper {

    public Role createCommandToRole(CreateRoleCommand command, String actor, Instant now) {
        return Role.builder()
                .uuid(UUID.randomUUID())
                .name(command.name().toUpperCase().trim())
                .description(command.description())
                .isSystem(false)
                .isActive(true)
                .isDeleted(false)
                .createdAt(now)
                .createdBy(actor)
                .updatedAt(now)
                .updatedBy(actor)
                .version(0L)
                .build();
    }

    public RoleResult roleToRoleResult(Role role, List<PermissionResult> permissions) {
        return new RoleResult(
                role.getUuid(),
                role.getName(),
                role.getDescription(),
                role.isSystem(),
                role.isActive(),
                permissions != null ? permissions : List.of(),
                role.getCreatedAt(),
                role.getUpdatedAt()
        );
    }
    public RoleSummary roleToRoleSummary(Role role) {
        return new RoleSummary(
                role.getUuid(),
                role.getName(),
                role.isActive()
        );
    }

}