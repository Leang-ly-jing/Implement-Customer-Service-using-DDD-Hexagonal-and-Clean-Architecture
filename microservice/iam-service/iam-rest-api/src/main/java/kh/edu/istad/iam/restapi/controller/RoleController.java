package kh.edu.istad.iam.restapi.controller;

import jakarta.validation.Valid;
import kh.edu.istad.iam.domain.dto.command.role.AssignPermissionsToRoleCommand;
import kh.edu.istad.iam.domain.dto.command.role.CreateRoleCommand;
import kh.edu.istad.iam.domain.dto.command.role.UpdateRoleCommand;
import kh.edu.istad.iam.domain.dto.query.RoleFilterQuery;
import kh.edu.istad.iam.domain.dto.result.PageResult;
import kh.edu.istad.iam.domain.dto.result.PermissionAssignmentResult;
import kh.edu.istad.iam.domain.dto.result.RoleResult;
import kh.edu.istad.iam.domain.dto.result.RoleSummary;
import kh.edu.istad.iam.domain.usecase.role.*;
import kh.edu.istad.iam.restapi.dto.request.AssignPermissionsToRoleRequest;
import kh.edu.istad.iam.restapi.dto.request.CreateRoleRequest;
import kh.edu.istad.iam.restapi.dto.request.UpdateRoleRequest;
import kh.edu.istad.iam.restapi.dto.response.PageResponse;
import kh.edu.istad.iam.restapi.dto.response.RoleResponse;
import kh.edu.istad.iam.domain.valueobject.AuditAction;
import kh.edu.istad.iam.restapi.mapper.RoleWebMapper;
import kh.edu.istad.iam.restapi.service.AuditLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
public class RoleController {

    private final CreateRoleUseCase createRoleUseCase;
    private final ListRoleUseCase listRoleUseCase;
    private final GetRoleUseCase getRoleUseCase;
    private final UpdateRoleUseCase updateRoleUseCase;
    private final DeleteRoleUseCase deleteRoleUseCase;
    private final ActivateRoleUseCase activateRoleUseCase;
    private final DeactivateRoleUseCase deactivateRoleUseCase;
    private final AssignPermissionsToRoleUseCase assignPermissionsToRoleUseCase;
    private final RevokePermissionFromRoleUseCase revokePermissionFromRoleUseCase;
    private final RoleWebMapper roleWebMapper;
    private final AuditLogService auditLogService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('ROLE_ADMIN') or hasAuthority('role:create')")
    public RoleResponse create(@Valid @RequestBody CreateRoleRequest request) {
        CreateRoleCommand command = roleWebMapper.toCommand(request);
        RoleResult result = createRoleUseCase.execute(command);
        auditLogService.record(AuditAction.ROLE_ASSIGNED, "ROLE", result.uuid().toString(), "Created role: " + result.name());
        return roleWebMapper.toResponse(result);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('ROLE_ADMIN') or hasAuthority('role:read')")
    public PageResponse<RoleSummary> list(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            @RequestParam(name = "sort", defaultValue = "createdAt,desc") String sort,
            @RequestParam(name = "search", required = false) String search,
            @RequestParam(name = "enabled", required = false) Boolean enabled
    ) {
        RoleFilterQuery query = new RoleFilterQuery(page, size, sort, search, enabled);
        PageResult<RoleSummary> pageResult = listRoleUseCase.execute(query);
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
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('ROLE_ADMIN') or hasAuthority('role:read')")
    public RoleResponse getByUuid(@PathVariable("uuid") UUID uuid) {
        RoleResult result = getRoleUseCase.execute(uuid);
        return roleWebMapper.toResponse(result);
    }

    @PatchMapping("/{uuid}")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('ROLE_ADMIN') or hasAuthority('role:update')")
    public RoleResponse update(@PathVariable("uuid") UUID uuid,
                               @Valid @RequestBody UpdateRoleRequest request) {
        UpdateRoleCommand command = roleWebMapper.toCommand(uuid, request);
        RoleResult result = updateRoleUseCase.execute(command);
        auditLogService.record(AuditAction.CONFIG_UPDATED, "ROLE", uuid.toString(), "Updated role: " + result.name());
        return roleWebMapper.toResponse(result);
    }

    @DeleteMapping("/{uuid}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('ROLE_ADMIN') or hasAuthority('role:delete')")
    public void delete(@PathVariable("uuid") UUID uuid) {
        deleteRoleUseCase.execute(uuid);
        auditLogService.record(AuditAction.CONFIG_UPDATED, "ROLE", uuid.toString(), "Deleted role");
    }

    @PostMapping("/{uuid}/activate")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('ROLE_ADMIN') or hasAuthority('role:update')")
    public void activate(@PathVariable("uuid") UUID uuid) {
        activateRoleUseCase.execute(uuid);
        auditLogService.record(AuditAction.CONFIG_UPDATED, "ROLE", uuid.toString(), "Activated role");
    }

    @PostMapping("/{uuid}/deactivate")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('ROLE_ADMIN') or hasAuthority('role:update')")
    public void deactivate(@PathVariable("uuid") UUID uuid) {
        deactivateRoleUseCase.execute(uuid);
        auditLogService.record(AuditAction.CONFIG_UPDATED, "ROLE", uuid.toString(), "Deactivated role");
    }

    @PostMapping("/{uuid}/permissions")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('ROLE_ADMIN') or hasAuthority('role:assign')")
    public List<PermissionAssignmentResult> assignPermissions(@PathVariable("uuid") UUID uuid,
                                                              @Valid @RequestBody AssignPermissionsToRoleRequest request) {
        AssignPermissionsToRoleCommand command = roleWebMapper.toCommand(uuid, request);
        List<PermissionAssignmentResult> results = assignPermissionsToRoleUseCase.execute(command);
        auditLogService.record(AuditAction.PERMISSION_UPDATED, "ROLE", uuid.toString(), "Assigned permissions: " + request.permissionUuids());
        return results;
    }

    @DeleteMapping("/{uuid}/permissions/{permissionUuid}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('ROLE_ADMIN') or hasAuthority('role:assign')")
    public void revokePermission(@PathVariable("uuid") UUID uuid, @PathVariable("permissionUuid") UUID permissionUuid,
                                 @RequestParam(name = "reason", defaultValue = "REVOKED") String reason) {
        revokePermissionFromRoleUseCase.execute(uuid, permissionUuid, reason);
        auditLogService.record(AuditAction.PERMISSION_UPDATED, "ROLE", uuid.toString(), "Revoked permission " + permissionUuid + " from role");
    }
}