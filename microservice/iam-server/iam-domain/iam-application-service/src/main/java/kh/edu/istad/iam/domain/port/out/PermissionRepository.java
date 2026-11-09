package kh.edu.istad.iam.domain.port.out;

import kh.edu.istad.iam.domain.entity.Permission;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface PermissionRepository {

    Permission save(Permission permission);

    Optional<Permission> findById(Long id);

    Optional<Permission> findByUuid(UUID uuid);

    Optional<Permission> findByName(String name);

    List<Permission> findByUuids(Set<UUID> uuids);

    boolean existsByName(String name);

    boolean existsByResourceAndAction(String resource, String action);

    List<Permission> findAll(int page, int size, String sort);

    long count();
}