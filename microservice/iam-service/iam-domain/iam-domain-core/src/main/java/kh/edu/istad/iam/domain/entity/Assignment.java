package kh.edu.istad.iam.domain.entity;

import java.time.Instant;


public class Assignment {
    private Long id;
    private Long userId;
    private Long roleId;
    private Long permissionId;
    private String effect;
    private Instant assignedAt;
    private String assignedBy;
    private Instant expiresAt;;
    private Instant revokedAt;
    private String revokedBy;
    private String reason;

    private Assignment(Builder builder) {
        id = builder.id;
        userId = builder.userId;
        roleId = builder.roleId;
        permissionId = builder.permissionId;
        effect = builder.effect;
        assignedAt = builder.assignedAt;
        assignedBy = builder.assignedBy;
        expiresAt = builder.expiresAt;
        revokedAt = builder.revokedAt;
        revokedBy = builder.revokedBy;
        reason = builder.reason;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public Long getPermissionId() {
        return permissionId;
    }

    public void setPermissionId(Long permissionId) {
        this.permissionId = permissionId;
    }

    public String getEffect() {
        return effect;
    }

    public void setEffect(String effect) {
        this.effect = effect;
    }

    public Instant getAssignedAt() {
        return assignedAt;
    }

    public void setAssignedAt(Instant assignedAt) {
        this.assignedAt = assignedAt;
    }

    public String getAssignedBy() {
        return assignedBy;
    }

    public void setAssignedBy(String assignedBy) {
        this.assignedBy = assignedBy;
    }

    public Instant getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(Instant expiresAt) {
        this.expiresAt = expiresAt;
    }

    public Instant getRevokedAt() {
        return revokedAt;
    }

    public void setRevokedAt(Instant revokedAt) {
        this.revokedAt = revokedAt;
    }

    public String getRevokedBy() {
        return revokedBy;
    }

    public void setRevokedBy(String revokedBy) {
        this.revokedBy = revokedBy;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public static final class Builder {
        private Long id;
        private Long userId;
        private Long roleId;
        private Long permissionId;
        private String effect;
        private Instant assignedAt;
        private String assignedBy;
        private Instant expiresAt;
        private Instant revokedAt;
        private String revokedBy;
        private String reason;

        private Builder() {
        }

        public static Builder builder() {
            return new Builder();
        }

        public Builder id(Long val) {
            id = val;
            return this;
        }

        public Builder userId(Long val) {
            userId = val;
            return this;
        }

        public Builder roleId(Long val) {
            roleId = val;
            return this;
        }

        public Builder permissionId(Long val) {
            permissionId = val;
            return this;
        }

        public Builder effect(String val) {
            effect = val;
            return this;
        }

        public Builder assignedAt(Instant val) {
            assignedAt = val;
            return this;
        }

        public Builder assignedBy(String val) {
            assignedBy = val;
            return this;
        }

        public Builder expiresAt(Instant val) {
            expiresAt = val;
            return this;
        }

        public Builder revokedAt(Instant val) {
            revokedAt = val;
            return this;
        }

        public Builder revokedBy(String val) {
            revokedBy = val;
            return this;
        }

        public Builder reason(String val) {
            reason = val;
            return this;
        }

        public Assignment build() {
            return new Assignment(this);
        }
    }
}
