package kh.edu.istad.iam.persistence.mapper;

import kh.edu.istad.iam.domain.entity.Identity;
import kh.edu.istad.iam.domain.entity.User;
import kh.edu.istad.iam.domain.entity.UserPermission;
import kh.edu.istad.iam.domain.entity.UserRole;
import kh.edu.istad.iam.domain.valueobject.*;
import kh.edu.istad.iam.persistence.entity.IdentityEntity;
import kh.edu.istad.iam.persistence.entity.UserEntity;
import kh.edu.istad.iam.persistence.entity.UserPermissionEntity;
import kh.edu.istad.iam.persistence.entity.UserRoleEntity;
import org.springframework.stereotype.Component;

@Component
public class UserPersistenceMapper {

    public UserEntity userToEntity(User user) {
        UserEntity entity = new UserEntity();
        entity.setId(user.getId());
        entity.setUuid(user.getUuid());
        entity.setUsername(user.getUsernameValue());
        entity.setEmail(user.getEmailValue());
        entity.setEmailVerified(user.isEmailVerified());
        entity.setPasswordHash(user.getPasswordHash());
        entity.setGivenName(user.getGivenName());
        entity.setFamilyName(user.getFamilyName());
        entity.setMiddleName(user.getMiddleName());
        entity.setNickname(user.getNickname());
        entity.setGender(user.getGender());
        entity.setBirthdate(user.getBirthdate());
        entity.setProfileUrl(user.getProfileUrl());
        entity.setPictureUrl(user.getPictureUrl());
        entity.setWebsiteUrl(user.getWebsiteUrl());
        entity.setZoneinfo(user.getZoneinfo());
        entity.setLocale(user.getLocale());
        entity.setPhoneNumber(user.getPhoneNumberValue());
        entity.setPhoneNumberVerified(user.isPhoneNumberVerified());

        if (user.getAddress() != null) {
            entity.setAddressStreetAddress(user.getAddress().streetAddress());
            entity.setAddressLocality(user.getAddress().locality());
            entity.setAddressRegion(user.getAddress().region());
            entity.setAddressPostalCode(user.getAddress().postalCode());
            entity.setAddressCountry(user.getAddress().country());
        }

        entity.setEnabled(user.isEnabled());
        entity.setAccountNonExpired(user.isAccountNonExpired());
        entity.setAccountNonLocked(user.isAccountNonLocked());
        entity.setCredentialsNonExpired(user.isCredentialsNonExpired());
        entity.setFailedLoginAttempts(user.getFailedLoginAttempts());
        entity.setLastLoginAt(user.getLastLoginAt());
        entity.setPasswordChangedAt(user.getPasswordChangedAt());
        entity.setDeleted(user.isDeleted());
        entity.setDeletedAt(user.getDeletedAt());
        entity.setDeletedBy(user.getDeletedBy());
        entity.setCreatedAt(user.getCreatedAt());
        entity.setCreatedBy(user.getCreatedBy());
        entity.setUpdatedAt(user.getUpdatedAt());
        entity.setUpdatedBy(user.getUpdatedBy());
        entity.setVersion(user.getVersion() != null ? user.getVersion() : 0L);

        return entity;
    }

    public User userToDomain(UserEntity entity) {
        Address address = null;
        if (entity.getAddressStreetAddress() != null || entity.getAddressCountry() != null) {
            address = new Address(
                    entity.getAddressStreetAddress(),
                    entity.getAddressLocality(),
                    entity.getAddressRegion(),
                    entity.getAddressPostalCode(),
                    entity.getAddressCountry()
            );
        }

        return User.builder()
                .id(entity.getId())
                .uuid(entity.getUuid())
                .username(entity.getUsername())
                .email(entity.getEmail())
                .emailVerified(entity.isEmailVerified())
                .passwordHash(entity.getPasswordHash())
                .givenName(entity.getGivenName())
                .familyName(entity.getFamilyName())
                .middleName(entity.getMiddleName())
                .nickname(entity.getNickname())
                .gender(entity.getGender())
                .birthdate(entity.getBirthdate())
                .profileUrl(entity.getProfileUrl())
                .pictureUrl(entity.getPictureUrl())
                .websiteUrl(entity.getWebsiteUrl())
                .zoneinfo(entity.getZoneinfo())
                .locale(entity.getLocale())
                .phoneNumber(entity.getPhoneNumber())
                .phoneNumberVerified(entity.isPhoneNumberVerified())
                .address(address)
                .enabled(entity.isEnabled())
                .accountNonExpired(entity.isAccountNonExpired())
                .accountNonLocked(entity.isAccountNonLocked())
                .credentialsNonExpired(entity.isCredentialsNonExpired())
                .failedLoginAttempts(entity.getFailedLoginAttempts())
                .lastLoginAt(entity.getLastLoginAt())
                .passwordChangedAt(entity.getPasswordChangedAt())
                .deleted(entity.isDeleted())
                .deletedAt(entity.getDeletedAt())
                .deletedBy(entity.getDeletedBy())
                .createdAt(entity.getCreatedAt())
                .createdBy(entity.getCreatedBy())
                .updatedAt(entity.getUpdatedAt())
                .updatedBy(entity.getUpdatedBy())
                .version(entity.getVersion())
                .build();
    }

    public UserRoleEntity userRoleToEntity(UserRole domain) {
        UserRoleEntity entity = new UserRoleEntity();
        entity.setId(domain.getId());
        entity.setUserId(domain.getUserId());
        entity.setRoleId(domain.getRoleId());
        entity.setAssignedAt(domain.getAssignedAt());
        entity.setAssignedBy(domain.getAssignedBy());
        entity.setExpiresAt(domain.getExpiresAt());
        entity.setRevokedAt(domain.getRevokedAt());
        entity.setRevokedBy(domain.getRevokedBy());
        entity.setReason(domain.getReason());
        return entity;
    }

    public UserRole entityToUserRole(UserRoleEntity entity) {
        return UserRole.builder()
                .id(entity.getId())
                .userId(entity.getUserId())
                .roleId(entity.getRoleId())
                .assignedAt(entity.getAssignedAt())
                .assignedBy(entity.getAssignedBy())
                .expiresAt(entity.getExpiresAt())
                .revokedAt(entity.getRevokedAt())
                .revokedBy(entity.getRevokedBy())
                .reason(entity.getReason())
                .build();
    }

    public UserPermissionEntity userPermissionToEntity(UserPermission domain) {
        UserPermissionEntity entity = new UserPermissionEntity();
        entity.setId(domain.getId());
        entity.setUserId(domain.getUserId());
        entity.setPermissionId(domain.getPermissionId());
        entity.setEffect(domain.getEffect().name());
        entity.setAssignedAt(domain.getAssignedAt());
        entity.setAssignedBy(domain.getAssignedBy());
        entity.setExpiresAt(domain.getExpiresAt());
        entity.setRevokedAt(domain.getRevokedAt());
        entity.setRevokedBy(domain.getRevokedBy());
        entity.setReason(domain.getReason());
        return entity;
    }

    public UserPermission entityToUserPermission(UserPermissionEntity entity) {
        return UserPermission.builder()
                .id(entity.getId())
                .userId(entity.getUserId())
                .permissionId(entity.getPermissionId())
                .effect(PermissionEffect.valueOf(entity.getEffect()))
                .assignedAt(entity.getAssignedAt())
                .assignedBy(entity.getAssignedBy())
                .expiresAt(entity.getExpiresAt())
                .revokedAt(entity.getRevokedAt())
                .revokedBy(entity.getRevokedBy())
                .reason(entity.getReason())
                .build();
    }

    public IdentityEntity identityToEntity(Identity domain) {
        IdentityEntity entity = new IdentityEntity();
        entity.setId(domain.getId());
        entity.setUuid(domain.getUuid());
        entity.setUserId(domain.getUserId());
        entity.setProvider(domain.getProvider().name());
        entity.setProviderSubject(domain.getProviderSubject());
        entity.setEmail(domain.getEmail());
        entity.setEmailVerified(domain.isEmailVerified());
        entity.setDisplayName(domain.getDisplayName());
        entity.setUsername(domain.getUsername());
        entity.setPictureUrl(domain.getPictureUrl());
        String attrs = domain.getAttributes();
        entity.setAttributes((attrs != null && !attrs.isBlank()) ? attrs : "{}");
        entity.setLinkedAt(domain.getLinkedAt());
        entity.setLastLoginAt(domain.getLastLoginAt());
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

    public Identity entityToIdentity(IdentityEntity entity) {
        return Identity.builder()
                .id(entity.getId())
                .uuid(entity.getUuid())
                .userId(entity.getUserId())
                .provider(IdentityProvider.valueOf(entity.getProvider()))
                .providerSubject(entity.getProviderSubject())
                .email(entity.getEmail())
                .emailVerified(entity.isEmailVerified())
                .displayName(entity.getDisplayName())
                .username(entity.getUsername())
                .pictureUrl(entity.getPictureUrl())
                .attributes(entity.getAttributes())
                .linkedAt(entity.getLinkedAt())
                .lastLoginAt(entity.getLastLoginAt())
                .deleted(entity.isDeleted())
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