package kh.edu.istad.iam.domain.entity;

import kh.edu.istad.iam.domain.exception.AuthorizationDomainException;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

public class Role extends BaseAuditEntity<Long> {
    private Long id;
    private UUID uuid;
    private String name;
    private String description;
    private boolean isSystem;
    private boolean isActive;
    private boolean isDeleted;
    private Long version;
    private Set<Permission> permissions;

    public Role() {}

    public Role(Long id, UUID uuid, String name, String description, boolean isSystem,
                boolean isActive, boolean isDeleted, Long version, Set<Permission> permissions,
                Instant createdAt, String createdBy, Instant updatedAt, String updatedBy,
                Instant deletedAt, String deletedBy) {
        super(createdAt, createdBy, updatedAt, updatedBy, deletedAt, deletedBy);
        this.id = id;
        this.uuid = uuid;
        this.name = name;
        this.description = description;
        this.isSystem = isSystem;
        this.isActive = isActive;
        this.isDeleted = isDeleted;
        this.version = version;
        this.permissions = permissions;
    }

    public void validateCanModify() {
        if (this.isSystem) {
            throw new AuthorizationDomainException("System role '" + this.name + "' cannot be modified or deleted");
        }
    }

    public void validateCanAssign() {
        if (!this.isActive || this.isDeleted) {
            throw new AuthorizationDomainException("Inactive or deleted role '" + this.name + "' cannot be assigned");
        }
    }

    public String getAuthority() {
        return "ROLE_" + this.name;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isSystem() {
        return isSystem;
    }

    public void setSystem(boolean system) {
        isSystem = system;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public Set<Permission> getPermissions() {
        return permissions;
    }

    public void setPermissions(Set<Permission> permissions) {
        this.permissions = permissions;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long id;
        private UUID uuid;
        private String name;
        private String description;
        private boolean isSystem = false;
        private boolean isActive = true;
        private boolean isDeleted = false;
        private Long version;
        private Set<Permission> permissions;
        private Instant createdAt;
        private String createdBy;
        private Instant updatedAt;
        private String updatedBy;
        private Instant deletedAt;
        private String deletedBy;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder uuid(UUID uuid) {
            this.uuid = uuid;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder isSystem(boolean isSystem) {
            this.isSystem = isSystem;
            return this;
        }

        public Builder isActive(boolean isActive) {
            this.isActive = isActive;
            return this;
        }

        public Builder isDeleted(boolean isDeleted) {
            this.isDeleted = isDeleted;
            return this;
        }

        public Builder version(Long version) {
            this.version = version;
            return this;
        }

        public Builder permissions(Set<Permission> permissions) {
            this.permissions = permissions;
            return this;
        }

        public Builder createdAt(Instant createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Builder createdBy(String createdBy) {
            this.createdBy = createdBy;
            return this;
        }

        public Builder updatedAt(Instant updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public Builder updatedBy(String updatedBy) {
            this.updatedBy = updatedBy;
            return this;
        }

        public Builder deletedAt(Instant deletedAt) {
            this.deletedAt = deletedAt;
            return this;
        }

        public Builder deletedBy(String deletedBy) {
            this.deletedBy = deletedBy;
            return this;
        }

        public Role build() {
            return new Role(id, uuid, name, description, isSystem, isActive, isDeleted, version,
                    permissions, createdAt, createdBy, updatedAt, updatedBy, deletedAt, deletedBy);
        }
    }
}
