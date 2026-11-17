package kh.edu.istad.iam.restapi.controller;

import jakarta.validation.Valid;
import kh.edu.istad.iam.domain.dto.command.user.*;
import kh.edu.istad.iam.domain.dto.query.UserFilterQuery;
import kh.edu.istad.iam.domain.dto.result.*;
import kh.edu.istad.iam.domain.usecase.identity.ListUserIdentitiesUseCase;
import kh.edu.istad.iam.domain.usecase.identity.UnlinkIdentityUseCase;
import kh.edu.istad.iam.domain.usecase.user.*;
import kh.edu.istad.iam.restapi.dto.request.*;
import kh.edu.istad.iam.restapi.dto.response.*;
import kh.edu.istad.iam.restapi.mapper.UserWebMapper;
import kh.edu.istad.iam.domain.valueobject.AuditAction;
import kh.edu.istad.iam.restapi.service.AuditLogService;
import kh.edu.istad.iam.restapi.service.UserSessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final CreateUserUseCase createUserUseCase;
    private final ListUserUseCase listUserUseCase;
    private final GetUserUseCase getUserUseCase;
    private final UpdateUserProfileUseCase updateUserProfileUseCase;
    private final DeleteUserUseCase deleteUserUseCase;
    private final EnableUserUseCase enableUserUseCase;
    private final DisableUserUseCase disableUserUseCase;
    private final LockUserUseCase lockUserUseCase;
    private final UnlockUserUseCase unlockUserUseCase;
    private final ResetUserPasswordUseCase resetUserPasswordUseCase;
    private final GetUserAuthoritiesUseCase getUserAuthoritiesUseCase;
    private final AssignRolesToUserUseCase assignRolesToUserUseCase;
    private final RevokeRoleFromUserUseCase revokeRoleFromUserUseCase;
    private final GrantPermissionsToUserUseCase grantPermissionsToUserUseCase;
    private final RevokePermissionFromUserUseCase revokePermissionFromUserUseCase;
    private final ListUserIdentitiesUseCase listUserIdentitiesUseCase;
    private final UnlinkIdentityUseCase unlinkIdentityUseCase;
    private final UserWebMapper userWebMapper;
    private final UserSessionService userSessionService;
    private final kh.edu.istad.iam.restapi.service.LoginHistoryService loginHistoryService;
    private final AuditLogService auditLogService;
    private final kh.edu.istad.iam.restapi.validator.PasswordPolicyValidator passwordPolicyValidator;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('ROLE_ADMIN') or hasAuthority('user:create')")
    public UserResponse create(@Valid @RequestBody CreateUserRequest request) {
        if (request.password() != null && !request.password().isBlank()) {
            passwordPolicyValidator.validate(request.password());
        }
        CreateUserCommand command = userWebMapper.toCommand(request);
        UserResult result = createUserUseCase.execute(command);
        auditLogService.record(AuditAction.USER_CREATED, "USER", result.uuid().toString(), "Created user @" + result.username());
        return userWebMapper.toResponse(result);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('ROLE_ADMIN') or hasAuthority('user:read')")
    public PageResponse<UserSummary> list(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            @RequestParam(name = "sort", defaultValue = "createdAt,desc") String sort,
            @RequestParam(name = "search", required = false) String search,
            @RequestParam(name = "enabled", required = false) Boolean enabled
    ) {
        UserFilterQuery query = new UserFilterQuery(page, size, sort, search, enabled);
        PageResult<UserSummary> pageResult = listUserUseCase.execute(query);
        return new PageResponse<>(
                pageResult.content(),
                new PageResponse.PageMetadata(
                        pageResult.page().number(),
                        pageResult.page().size(),
                        pageResult.page().totalElements(),
                        pageResult.page().totalPages()
                )
        );
    }

    @GetMapping("/{uuid}")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('ROLE_ADMIN') or hasAuthority('user:read')")
    public UserResponse getByUuid(@PathVariable("uuid") UUID uuid) {
        UserResult result = getUserUseCase.execute(uuid);
        return userWebMapper.toResponse(result);
    }

    @PatchMapping("/{uuid}")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('ROLE_ADMIN') or hasAuthority('user:update')")
    public UserResponse updateProfile(@PathVariable("uuid") UUID uuid,
                                      @Valid @RequestBody UpdateUserProfileRequest request) {
        UpdateUserProfileCommand command = userWebMapper.toCommand(uuid, request);
        UserResult result = updateUserProfileUseCase.execute(command);
        auditLogService.record(AuditAction.USER_UPDATED, "USER", uuid.toString(), "Updated profile for @" + result.username());
        return userWebMapper.toResponse(result);
    }

    @DeleteMapping("/{uuid}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('ROLE_ADMIN') or hasAuthority('user:delete')")
    public void delete(@PathVariable("uuid") UUID uuid) {
        deleteUserUseCase.execute(uuid);
        userSessionService.revokeAllForUser(uuid);
        auditLogService.record(AuditAction.USER_DELETED, "USER", uuid.toString(), "Deleted user account");
    }

    @PostMapping("/{uuid}/enable")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('ROLE_ADMIN') or hasAuthority('user:update')")
    public void enable(@PathVariable("uuid") UUID uuid) {
        enableUserUseCase.execute(uuid);
        auditLogService.record(AuditAction.ACCOUNT_ENABLED, "USER", uuid.toString(), "Enabled user account");
    }

    @PostMapping("/{uuid}/disable")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('ROLE_ADMIN') or hasAuthority('user:update')")
    public void disable(@PathVariable("uuid") UUID uuid) {
        disableUserUseCase.execute(uuid);
        userSessionService.revokeAllForUser(uuid);
        auditLogService.record(AuditAction.ACCOUNT_DISABLED, "USER", uuid.toString(), "Disabled user account");
    }

    @PostMapping("/{uuid}/lock")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('ROLE_ADMIN') or hasAuthority('user:update')")
    public void lock(@PathVariable("uuid") UUID uuid) {
        lockUserUseCase.execute(uuid);
        userSessionService.revokeAllForUser(uuid);
        auditLogService.record(AuditAction.ACCOUNT_LOCKED, "USER", uuid.toString(), "Locked user account");
    }

    @PostMapping("/{uuid}/unlock")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('ROLE_ADMIN') or hasAuthority('user:update')")
    public void unlock(@PathVariable("uuid") UUID uuid) {
        unlockUserUseCase.execute(uuid);
        auditLogService.record(AuditAction.ACCOUNT_UNLOCKED, "USER", uuid.toString(), "Unlocked user account");
    }

    @PutMapping("/{uuid}/password")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('ROLE_ADMIN') or hasAuthority('user:update')")
    public void resetPassword(@PathVariable("uuid") UUID uuid,
                              @Valid @RequestBody ResetPasswordRequest request) {
        passwordPolicyValidator.validate(request.newPassword());
        resetUserPasswordUseCase.execute(new ResetUserPasswordCommand(uuid, request.newPassword()));
        userSessionService.revokeAllForUser(uuid);
        auditLogService.record(AuditAction.PASSWORD_RESET, "USER", uuid.toString(), "Reset user password by admin");
    }

    @GetMapping("/{uuid}/authorities")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('ROLE_ADMIN') or hasAuthority('user:read')")
    public UserAuthoritiesResponse getAuthorities(@PathVariable("uuid") UUID uuid) {
        UserAuthoritiesResult result = getUserAuthoritiesUseCase.execute(uuid);
        return userWebMapper.toResponse(result);
    }

    @PostMapping("/{uuid}/roles")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('ROLE_ADMIN') or hasAuthority('user:assign')")
    public List<RoleAssignmentResult> assignRoles(@PathVariable("uuid") UUID uuid,
                                                  @Valid @RequestBody AssignRolesToUserRequest request) {
        AssignRolesToUserCommand command = userWebMapper.toCommand(uuid, request);
        List<RoleAssignmentResult> results = assignRolesToUserUseCase.execute(command);
        auditLogService.record(AuditAction.ROLE_ASSIGNED, "USER", uuid.toString(), "Assigned roles: " + request.roleUuids());
        return results;
    }

    @DeleteMapping("/{uuid}/roles/{roleUuid}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('ROLE_ADMIN') or hasAuthority('user:assign')")
    public void revokeRole(@PathVariable("uuid") UUID uuid, @PathVariable("roleUuid") UUID roleUuid,
                           @RequestParam(name = "reason", defaultValue = "REVOKED") String reason) {
        revokeRoleFromUserUseCase.execute(uuid, roleUuid, reason);
        auditLogService.record(AuditAction.ROLE_REVOKED, "USER", uuid.toString(), "Revoked role " + roleUuid + " (reason: " + reason + ")");
    }

    @PostMapping("/{uuid}/permissions")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('ROLE_ADMIN') or hasAuthority('user:assign')")
    public List<PermissionGrantResult> grantPermissions(@PathVariable("uuid") UUID uuid,
                                                        @Valid @RequestBody GrantPermissionsToUserRequest request) {
        GrantPermissionsToUserCommand command = userWebMapper.toCommand(uuid, request);
        List<PermissionGrantResult> results = grantPermissionsToUserUseCase.execute(command);
        auditLogService.record(AuditAction.PERMISSION_UPDATED, "USER", uuid.toString(), "Granted permissions: " + request.permissionUuids() + " (" + request.effect() + ")");
        return results;
    }

    @DeleteMapping("/{uuid}/permissions/{permissionUuid}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('ROLE_ADMIN') or hasAuthority('user:assign')")
    public void revokePermission(@PathVariable("uuid") UUID uuid, @PathVariable("permissionUuid") UUID permissionUuid,
                                 @RequestParam(name = "reason", defaultValue = "REVOKED") String reason) {
        revokePermissionFromUserUseCase.execute(uuid, permissionUuid, reason);
        auditLogService.record(AuditAction.PERMISSION_UPDATED, "USER", uuid.toString(), "Revoked permission: " + permissionUuid);
    }

    @GetMapping("/{uuid}/identities")
    @PreAuthorize("hasAuthority('user:read')")
    public List<IdentityResponse> listIdentities(@PathVariable("uuid") UUID uuid) {
        return listUserIdentitiesUseCase.execute(uuid).stream()
                .map(userWebMapper::toResponse)
                .toList();
    }

    @DeleteMapping("/{uuid}/identities/{identityUuid}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('user:update')")
    public void unlinkIdentity(@PathVariable("uuid") UUID uuid,
                               @PathVariable("identityUuid") UUID identityUuid) {
        unlinkIdentityUseCase.execute(uuid, identityUuid);
    }

    @GetMapping("/{uuid}/sessions")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('user:read')")
    public List<UserSessionResponse> getUserSessions(@PathVariable("uuid") UUID uuid) {
        return userSessionService.getSessionsForUser(uuid);
    }

    @DeleteMapping("/{uuid}/sessions/{sessionId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('user:update')")
    public void terminateUserSession(@PathVariable("uuid") UUID uuid,
                                     @PathVariable("sessionId") UUID sessionId) {
        userSessionService.adminRevokeSession(uuid, sessionId);
    }

    @DeleteMapping("/{uuid}/sessions")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('user:update')")
    public void terminateAllUserSessions(@PathVariable("uuid") UUID uuid) {
        userSessionService.adminRevokeAllSessions(uuid);
    }

    @GetMapping("/{uuid}/login-history")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('user:read')")
    public PageResponse<LoginHistoryResponse> getUserLoginHistory(
            @PathVariable("uuid") UUID uuid,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "20") int size
    ) {
        return loginHistoryService.getUserLoginHistory(uuid, page, size);
    }
}