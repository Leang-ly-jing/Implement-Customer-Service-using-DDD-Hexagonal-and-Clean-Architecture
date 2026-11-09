package kh.edu.istad.iam.domain.port.out;

import kh.edu.istad.iam.domain.entity.Role;
import kh.edu.istad.iam.domain.entity.RolePermission;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface RoleRepository {

    Role save(Role role);

    Optional<Role> findById(Long id);

    Optional<Role> findByUuid(UUID uuid);

    Optional<Role> findByName(String name);

    List<Role> findByUuids(Set<UUID> uuids);

    boolean existsByName(String name);

    List<Role> findAll(int page, int size, String sort);

    long count();

    RolePermission saveRolePermission(RolePermission rolePermission);

    List<RolePermission> findPermissionsByRoleId(Long roleId);

    Optional<RolePermission> findActiveRolePermission(Long roleId, Long permissionId);
}
