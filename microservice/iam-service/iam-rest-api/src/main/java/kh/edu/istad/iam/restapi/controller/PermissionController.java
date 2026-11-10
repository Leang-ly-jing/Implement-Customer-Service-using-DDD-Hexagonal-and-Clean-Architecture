package kh.edu.istad.iam.restapi.controller;

import jakarta.validation.Valid;
import kh.edu.istad.iam.domain.dto.command.permission.CreatePermissionCommand;
import kh.edu.istad.iam.domain.dto.command.permission.UpdatePermissionCommand;
import kh.edu.istad.iam.domain.dto.query.PermissionFilterQuery;
import kh.edu.istad.iam.domain.dto.result.PageResult;
import kh.edu.istad.iam.domain.dto.result.PermissionResult;
import kh.edu.istad.iam.domain.usecase.Permission.*;
import kh.edu.istad.iam.restapi.dto.request.CreatePermissionRequest;
import kh.edu.istad.iam.restapi.dto.request.UpdatePermissionRequest;
import kh.edu.istad.iam.restapi.dto.response.PageResponse;
import kh.edu.istad.iam.restapi.dto.response.PermissionResponse;
import kh.edu.istad.iam.restapi.mapper.PermissionWebMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/permissions")
@RequiredArgsConstructor
public class PermissionController {

    private final CreatePermissionUseCase createPermissionUseCase;
    private final ListPermissionUseCase listPermissionUseCase;
    private final GetPermissionUseCase getPermissionUseCase;
    private final UpdatePermissionUseCase updatePermissionUseCase;
    private final DeletePermissionUseCase deletePermissionUseCase;
    private final ActivatePermissionUseCase activatePermissionUseCase;
    private final DeactivatePermissionUseCase deactivatePermissionUseCase;
    private final PermissionWebMapper permissionWebMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('permission:create')")
    public PermissionResponse create(@Valid @RequestBody
                                         CreatePermissionRequest request) {
        CreatePermissionCommand command = permissionWebMapper.toCommand(request);
        PermissionResult result = createPermissionUseCase.execute(command);
        return permissionWebMapper.toResponse(result);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('permission:read')")
    public PageResponse<PermissionResult> list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name,asc") String sort,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Boolean enabled
    ) {
        PermissionFilterQuery query = new PermissionFilterQuery(page, size, sort, search, enabled);
        PageResult<PermissionResult> pageResult = listPermissionUseCase.execute(query);
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
    @PreAuthorize("hasAuthority('permission:read')")
    public PermissionResponse getByUuid(@PathVariable UUID uuid) {
        PermissionResult result = getPermissionUseCase.execute(uuid);
        return permissionWebMapper.toResponse(result);
    }

    @PatchMapping("/{uuid}")
    @PreAuthorize("hasAuthority('permission:update')")
    public PermissionResponse update(@PathVariable UUID uuid,
                                     @Valid @RequestBody UpdatePermissionRequest request) {
        UpdatePermissionCommand command = permissionWebMapper.toCommand(uuid, request);
        PermissionResult result = updatePermissionUseCase.execute(command);
        return permissionWebMapper.toResponse(result);
    }

    @DeleteMapping("/{uuid}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('permission:delete')")
    public void delete(@PathVariable UUID uuid) {
        deletePermissionUseCase.execute(uuid);
    }

    @PostMapping("/{uuid}/activate")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('permission:update')")
    public void activate(@PathVariable UUID uuid) {
        activatePermissionUseCase.execute(uuid);
    }

    @PostMapping("/{uuid}/deactivate")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('permission:update')")
    public void deactivate(@PathVariable UUID uuid) {
        deactivatePermissionUseCase.execute(uuid);
    }
}