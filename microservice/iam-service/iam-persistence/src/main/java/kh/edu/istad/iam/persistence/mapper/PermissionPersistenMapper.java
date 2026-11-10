package kh.edu.istad.iam.persistence.mapper;

import kh.edu.istad.iam.domain.entity.Permission;
import kh.edu.istad.iam.persistence.entity.PermissionEntity;
import org.springframework.stereotype.Component;

@Component
public class PermissionPersistenMapper {

    public PermissionEntity permissionToEntity(Permission domain) {
        PermissionEntity entity = new PermissionEntity();
        entity.setId(domain.getId());
        entity.setUuid(domain.getUuid());
        entity.setName(domain.getName());
        entity.setResource(domain.getResource());
        entity.setAction(domain.getAction());
        entity.setDescription(domain.getDescription());
        entity.setSystem(domain.isSystem());
        entity.setActive(domain.isActive());
        entity.setDeleted(domain.isDeleted());
        entity.setDeletedAt(domain.getDeletedAt());
        entity.setDeletedBy(domain.getDeletedBy());
        entity.setCreatedAt(domain.getCreatedAt());
        entity.setCreatedBy(domain.getCreatedBy());
        entity.setUpdatedAt(domain.getUpdatedAt());
        entity.setUpdatedBy(domain.getUpdatedBy());
        entity.setVersion(domain.getVersion() != null ? domain.getVersion() : 0L);
        return entity;
    }

    public Permission entityToDomain(PermissionEntity entity) {
        return Permission.builder()
                .id(entity.getId())
                .uuid(entity.getUuid())
                .name(entity.getName())
                .resource(entity.getResource())
                .action(entity.getAction())
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
}