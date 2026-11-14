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

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('user:create')")
    public UserResponse create(@Valid @RequestBody CreateUserRequest request) {
        CreateUserCommand command = userWebMapper.toCommand(request);
        UserResult result = createUserUseCase.execute(command);
        return userWebMapper.toResponse(result);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('user:read')")
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
    @PreAuthorize("hasAuthority('user:read')")
    public UserResponse getByUuid(@PathVariable("uuid") UUID uuid) {
        UserResult result = getUserUseCase.execute(uuid);
        return userWebMapper.toResponse(result);
    }

    @PatchMapping("/{uuid}")
    @PreAuthorize("hasAuthority('user:update')")
    public UserResponse updateProfile(@PathVariable("uuid") UUID uuid,
                                      @Valid @RequestBody UpdateUserProfileRequest request) {
        UpdateUserProfileCommand command = userWebMapper.toCommand(uuid, request);
        UserResult result = updateUserProfileUseCase.execute(command);
        return userWebMapper.toResponse(result);
    }

    @DeleteMapping("/{uuid}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('user:delete')")
    public void delete(@PathVariable("uuid") UUID uuid) {
        deleteUserUseCase.execute(uuid);
    }

    @PostMapping("/{uuid}/enable")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('user:update')")
    public void enable(@PathVariable("uuid") UUID uuid) {
        enableUserUseCase.execute(uuid);
    }

    @PostMapping("/{uuid}/disable")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('user:update')")
    public void disable(@PathVariable("uuid") UUID uuid) {
        disableUserUseCase.execute(uuid);
    }

    @PostMapping("/{uuid}/lock")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('user:update')")
    public void lock(@PathVariable("uuid") UUID uuid) {
        lockUserUseCase.execute(uuid);
    }

    @PostMapping("/{uuid}/unlock")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('user:update')")
    public void unlock(@PathVariable("uuid") UUID uuid) {
        unlockUserUseCase.execute(uuid);
    }

    @PutMapping("/{uuid}/password")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('user:update')")
    public void resetPassword(@PathVariable("uuid") UUID uuid,
                              @Valid @RequestBody ResetPasswordRequest request) {
        resetUserPasswordUseCase.execute(new ResetUserPasswordCommand(uuid, request.newPassword()));
    }

    @GetMapping("/{uuid}/authorities")
    @PreAuthorize("hasAuthority('user:read')")
    public UserAuthoritiesResponse getAuthorities(@PathVariable("uuid") UUID uuid) {
        UserAuthoritiesResult result = getUserAuthoritiesUseCase.execute(uuid);
        return userWebMapper.toResponse(result);
    }

    @PostMapping("/{uuid}/roles")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('user:assign')")
    public List<RoleAssignmentResult> assignRoles(@PathVariable("uuid") UUID uuid,
                                                  @Valid @RequestBody AssignRolesToUserRequest request) {
        AssignRolesToUserCommand command = userWebMapper.toCommand(uuid, request);
        return assignRolesToUserUseCase.execute(command);
    }

    @DeleteMapping("/{uuid}/roles/{roleUuid}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('user:assign')")
    public void revokeRole(@PathVariable("uuid") UUID uuid, @PathVariable("roleUuid") UUID roleUuid,
                           @RequestParam(name = "reason", defaultValue = "REVOKED") String reason) {
        revokeRoleFromUserUseCase.execute(uuid, roleUuid, reason);
    }

    @PostMapping("/{uuid}/permissions")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('user:assign')")
    public List<PermissionGrantResult> grantPermissions(@PathVariable("uuid") UUID uuid,
                                                        @Valid @RequestBody GrantPermissionsToUserRequest request) {
        GrantPermissionsToUserCommand command = userWebMapper.toCommand(uuid, request);
        return grantPermissionsToUserUseCase.execute(command);
    }

    @DeleteMapping("/{uuid}/permissions/{permissionUuid}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('user:assign')")
    public void revokePermission(@PathVariable("uuid") UUID uuid, @PathVariable("permissionUuid") UUID permissionUuid,
                                 @RequestParam(name = "reason", defaultValue = "REVOKED") String reason) {
        revokePermissionFromUserUseCase.execute(uuid, permissionUuid, reason);
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
}