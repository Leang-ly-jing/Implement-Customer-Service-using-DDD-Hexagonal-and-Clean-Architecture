package kh.edu.istad.iam.domain.usecase.role;

import kh.edu.istad.iam.domain.dto.result.PermissionResult;
import kh.edu.istad.iam.domain.dto.result.RoleResult;
import kh.edu.istad.iam.domain.entity.Role;
import kh.edu.istad.iam.domain.entity.RolePermission;
import kh.edu.istad.iam.domain.exception.AuthorizationDomainException;
import kh.edu.istad.iam.domain.mapper.PermissionDataMapper;
import kh.edu.istad.iam.domain.mapper.RoleDataMapper;
import kh.edu.istad.iam.domain.port.out.PermissionRepository;
import kh.edu.istad.iam.domain.port.out.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
@Component
@RequiredArgsConstructor
public class GetRoleUseCase {

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final RoleDataMapper roleDataMapper;
    private final PermissionDataMapper permissionDataMapper;

    public RoleResult execute(UUID roleUuid) {
        Role role = roleRepository.findByUuid(roleUuid)
                .orElseThrow(() -> new AuthorizationDomainException("Role not found"));

        List<RolePermission> rps = roleRepository.findPermissionsByRoleId(role.getId());
        List<PermissionResult> permissions = new ArrayList<>();
        Set<Long> seenIds = new HashSet<>();
        for (RolePermission rp : rps) {
            if (rp.isActive() && seenIds.add(rp.getPermissionId())) {
                permissionRepository.findById(rp.getPermissionId()).ifPresent(p ->
                        permissions.add(permissionDataMapper.permissionToPermissionResult(p)));
            }
        }

        return roleDataMapper.roleToRoleResult(role, permissions);
    }
}