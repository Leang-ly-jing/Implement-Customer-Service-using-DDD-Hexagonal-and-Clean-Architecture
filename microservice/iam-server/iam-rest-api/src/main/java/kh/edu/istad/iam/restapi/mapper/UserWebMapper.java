package kh.edu.istad.iam.restapi.mapper;

import kh.edu.istad.iam.domain.dto.command.user.*;
import kh.edu.istad.iam.domain.dto.result.IdentityResult;
import kh.edu.istad.iam.domain.dto.result.UserResult;
import kh.edu.istad.iam.domain.valueobject.Address;
import kh.edu.istad.iam.domain.valueobject.PermissionEffect;
import kh.edu.istad.iam.restapi.dto.request.*;
import kh.edu.istad.iam.restapi.dto.response.IdentityResponse;
import kh.edu.istad.iam.restapi.dto.response.UserAuthoritiesResponse;
import kh.edu.istad.iam.restapi.dto.response.UserResponse;
import org.springframework.stereotype.Component;
import kh.edu.istad.iam.domain.dto.result.UserAuthoritiesResult;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Component
public class UserWebMapper {

    public RegisterUserCommand toCommand(RegisterUserRequest request){
        return new RegisterUserCommand(
                request.userName(),
                request.email(),
                request.password(),
                request.confirmPassword(),
                request.givenName(),
                request.familyName(),
                request.locate()
        );
    }

    public CreateUserCommand toCommand(CreateUserRequest request){
        Address address = null;
        if (request.addressRequest() != null){
            address = new Address(
                    request.addressRequest().streetAddress(),
                    request.addressRequest().locality(),
                    request.addressRequest().region(),
                    request.addressRequest().postalCode(),
                    request.addressRequest().country()
            );
        }
        Set<UUID> rolesUuids = request.rolesUuids() != null ?
                new HashSet<>(request.rolesUuids())
                : Set.of();

        return new CreateUserCommand(
                request.userName(),
                request.email(),
                request.password(),
                request.givenName(),
                request.familyName(),
                request.gender(),
                request.birthDate(),
                request.phoneNumber(),
                request.zoneInfo(),
                request.locate(),
                address,
                rolesUuids
        );
    }

    public UpdateUserProfileCommand toCommand(UUID uuid, UpdateUserProfileRequest request) {
        return new UpdateUserProfileCommand(
                uuid,
                request.givenName(),
                request.familyName(),
                request.nickname(),
                request.gender(),
                request.phoneNumber(),
                request.locale()
        );
    }
    public AssignRolesToUserCommand toCommand(UUID uuid, AssignRolesToUserRequest request) {
        return new AssignRolesToUserCommand(
                uuid,
                request.roleUuids() != null ? new HashSet<>(request.roleUuids()) : Set.of(),
                request.expiresAt(),
                request.reason()
        );
    }
    public GrantPermissionsToUserCommand toCommand(UUID uuid, GrantPermissionsToUserRequest request) {
        Set<UUID> permissionUuids = request.permissionUuids() != null
                ? new HashSet<>(request.permissionUuids())
                : Set.of();

        PermissionEffect effect = request.effect() != null
                ? PermissionEffect.valueOf(request.effect().toUpperCase())
                : PermissionEffect.GRANT;

        return new GrantPermissionsToUserCommand(
                uuid,
                permissionUuids,
                effect,
                null,
                request.reason()
        );
    }
    public UserResponse toResponse(UserResult result) {
        List<UserResponse.RoleSummaryResponse> roles = List.of();
        if (result.roles() != null) {
            roles = result.roles().stream()
                    .map(r -> new UserResponse.RoleSummaryResponse(r.uuid(), r.name()))
                    .toList();
        }
        return new UserResponse(
                result.uuid(),
                result.username(),
                result.email(),
                result.emailVerified(),
                result.name(),
                result.locale(),
                result.enabled(),
                roles,
                result.createdAt(),
                result.updatedAt()
        );
    }
    public UserAuthoritiesResponse toResponse(UserAuthoritiesResult result) {
        List<UserAuthoritiesResponse.RoleAuthority> roles = List.of();
        if (result.roles() != null) {
            roles = result.roles().stream()
                    .map(r -> new UserAuthoritiesResponse.RoleAuthority(r.uuid(), r.name(), r.expiresAt()))
                    .toList();
        }
        List<UserAuthoritiesResponse.DirectPermission> directPermissions = List.of();
        if (result.directPermissions() != null) {
            directPermissions = result.directPermissions().stream()
                    .map(p -> new UserAuthoritiesResponse.DirectPermission(p.name(), p.effect()))
                    .toList();
        }
        return new UserAuthoritiesResponse(
                roles,
                directPermissions,
                result.effectivePermissions() != null ? result.effectivePermissions().stream().toList() : List.of()
        );
    }
    public IdentityResponse toResponse(IdentityResult result) {
        return new IdentityResponse(
                result.uuid(),
                result.provider(),
                result.email(),
                result.displayName(),
                result.linkedAt()
        );
    }

}
