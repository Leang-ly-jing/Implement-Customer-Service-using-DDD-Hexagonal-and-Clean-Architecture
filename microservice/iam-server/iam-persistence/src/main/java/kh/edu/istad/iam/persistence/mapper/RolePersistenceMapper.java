package kh.edu.istad.iam.persistence.mapper;

import kh.edu.istad.iam.domain.entity.Role;
import kh.edu.istad.iam.domain.entity.RolePermission;
import kh.edu.istad.iam.persistence.entity.RoleEntity;
import kh.edu.istad.iam.persistence.entity.RolePermissionEntity;
import org.springframework.stereotype.Component;

@Component
public class RolePersistenceMapper {

    public RoleEntity roleToEntity(Role role) {
        RoleEntity entity = new RoleEntity();
        entity.setId(role.getId());
        entity.setUuid(role.getUuid());
        entity.setName(role.getName());
        entity.setDescription(role.getDescription());
        entity.setSystem(role.isSystem());
        entity.setActive(role.isActive());
        entity.setDeleted(role.isDeleted());
        entity.setDeletedAt(role.getDeletedAt());
        entity.setDeletedBy(role.getDeletedBy());
        entity.setCreatedAt(role.getCreatedAt());
        entity.setCreatedBy(role.getCreatedBy());
        entity.setUpdatedAt(role.getUpdatedAt());
        entity.setUpdatedBy(role.getUpdatedBy());
        entity.setVersion(role.getVersion() != null ? role.getVersion() : 0L);
        return entity;
    }

    public Role entityToDomain(RoleEntity entity) {
        return Role.builder()
                .id(entity.getId())
                .uuid(entity.getUuid())
                .name(entity.getName())
                .description(entity.getDescription())
                .isSystem(entity.isSystem())
                .isActive(entity.isActive())
                .isDeleted(entity.isDeleted())
                .deletedAt(entity.getDeletedAt())
                .deletedBy(entity.getDeletedBy())
                .createdAt(entity.getCreatedAt())
                .createdBy(entity.getCreatedBy())
                .updatedAt(entity.getUpdatedAt())
                .updatedBy(entity.getUpdatedBy())
                .version(entity.getVersion())
                .build();
    }

    public RolePermissionEntity rolePermissionToEntity(RolePermission domain) {
        RolePermissionEntity entity = new RolePermissionEntity();
        entity.setId(domain.getId());
        entity.setRoleId(domain.getRoleId());
        entity.setPermissionId(domain.getPermissionId());
        entity.setAssignedAt(domain.getAssignedAt());
        entity.setAssignedBy(domain.getAssignedBy());
        entity.setExpiresAt(domain.getExpiresAt());
        entity.setRevokedAt(domain.getRevokedAt());
        entity.setRevokedBy(domain.getRevokedBy());
        entity.setReason(domain.getReason());
        return entity;
    }

    public RolePermission entityToRolePermission(RolePermissionEntity entity) {
        return RolePermission.builder()
                .id(entity.getId())
                .roleId(entity.getRoleId())
                .permissionId(entity.getPermissionId())
                .assignedAt(entity.getAssignedAt())
                .assignedBy(entity.getAssignedBy())
                .expiresAt(entity.getExpiresAt())
                .revokedAt(entity.getRevokedAt())
                .revokedBy(entity.getRevokedBy())
                .reason(entity.getReason())
                .build();
    }
}