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
import kh.edu.istad.iam.restapi.mapper.RoleWebMapper;
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

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('role:create')")
    public RoleResponse create(@Valid @RequestBody
                                   CreateRoleRequest request) {
        CreateRoleCommand command = roleWebMapper.toCommand(request);
        RoleResult result = createRoleUseCase.execute(command);
        return roleWebMapper.toResponse(result);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('role:read')")
    public PageResponse<RoleSummary> list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt,desc") String sort,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Boolean enabled
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
    @PreAuthorize("hasAuthority('role:read')")
    public RoleResponse getByUuid(@PathVariable UUID uuid) {
        RoleResult result = getRoleUseCase.execute(uuid);
        return roleWebMapper.toResponse(result);
    }

    @PatchMapping("/{uuid}")
    @PreAuthorize("hasAuthority('role:update')")
    public RoleResponse update(@PathVariable UUID uuid,
                               @Valid @RequestBody UpdateRoleRequest request) {
        UpdateRoleCommand command = roleWebMapper.toCommand(uuid, request);
        RoleResult result = updateRoleUseCase.execute(command);
        return roleWebMapper.toResponse(result);
    }

    @DeleteMapping("/{uuid}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('role:delete')")
    public void delete(@PathVariable UUID uuid) {
        deleteRoleUseCase.execute(uuid);
    }

    @PostMapping("/{uuid}/activate")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('role:update')")
    public void activate(@PathVariable UUID uuid) {
        activateRoleUseCase.execute(uuid);
    }

    @PostMapping("/{uuid}/deactivate")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('role:update')")
    public void deactivate(@PathVariable UUID uuid) {
        deactivateRoleUseCase.execute(uuid);
    }

    @PostMapping("/{uuid}/permissions")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('role:assign')")
    public List<PermissionAssignmentResult> assignPermissions(@PathVariable UUID uuid,
                                                              @Valid @RequestBody AssignPermissionsToRoleRequest request) {
        AssignPermissionsToRoleCommand command = roleWebMapper.toCommand(uuid, request);
        return assignPermissionsToRoleUseCase.execute(command);
    }

    @DeleteMapping("/{uuid}/permissions/{permissionUuid}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('role:assign')")
    public void revokePermission(@PathVariable UUID uuid, @PathVariable UUID permissionUuid,
                                 @RequestParam(defaultValue = "REVOKED") String reason) {
        revokePermissionFromRoleUseCase.execute(uuid, permissionUuid, reason);
    }
}