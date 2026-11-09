package kh.edu.istad.iam.domain.mapper;

import kh.edu.istad.iam.domain.dto.command.permission.CreatePermissionCommand;
import kh.edu.istad.iam.domain.dto.result.PermissionResult;
import kh.edu.istad.iam.domain.entity.Permission;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.UUID;

@Component
public class PermissionDataMapper {

    public Permission createCommandToPermission(CreatePermissionCommand command, String actor, Instant now) {
        String resource = command.resource().trim().toLowerCase();
        String action = command.action().trim().toLowerCase();
        return Permission.builder()
                .uuid(UUID.randomUUID())
                .resource(resource)
                .action(action)
                .name(resource + ":" + action)
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

    public PermissionResult permissionToPermissionResult(Permission permission) {
        return new PermissionResult(
                permission.getUuid(),
                permission.getName(),
                permission.getResource(),
                permission.getAction(),
                permission.getDescription(),
                permission.isSystem(),
                permission.isActive(),
                permission.getCreatedAt(),
                permission.getUpdatedAt()
        );
    }
}
