package kh.edu.istad.iam.persistence.adapter;

import kh.edu.istad.iam.domain.entity.Role;
import kh.edu.istad.iam.domain.entity.RolePermission;
import kh.edu.istad.iam.domain.port.out.RoleRepository;
import kh.edu.istad.iam.persistence.entity.RoleEntity;
import kh.edu.istad.iam.persistence.entity.RolePermissionEntity;
import kh.edu.istad.iam.persistence.mapper.RolePersistenceMapper;
import kh.edu.istad.iam.persistence.repository.RoleJpaRepository;
import kh.edu.istad.iam.persistence.repository.RolePermissionJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
@Component
@RequiredArgsConstructor
public class RoleRepositoryAdapter implements RoleRepository {

    private final RoleJpaRepository roleJpaRepository;
    private final RolePermissionJpaRepository rolePermissionJpaRepository;
    private final RolePersistenceMapper rolePersistenceMapper;


    @Override
    public Role save(Role role) {
        RoleEntity roleEntity = rolePersistenceMapper.roleToEntity(role);
        RoleEntity saved = roleJpaRepository.save(roleEntity);
        return rolePersistenceMapper.entityToDomain(saved);
    }

    @Override
    public Optional<Role> findById(Long id) {
        return roleJpaRepository.findById(id)
                .map(rolePersistenceMapper::entityToDomain);
    }

    @Override
    public Optional<Role> findByUuid(UUID uuid) {
        return roleJpaRepository.findByUuidAndIsDeletedFalse(uuid)
                .map(rolePersistenceMapper::entityToDomain);
    }

    @Override
    public Optional<Role> findByName(String name) {
        return roleJpaRepository.findByNameAndIsDeletedFalse(name)
                .map(rolePersistenceMapper::entityToDomain);
    }

    @Override
    public List<Role> findByUuids(Set<UUID> uuids) {
        return roleJpaRepository.findByUuidInAndIsDeletedFalse(uuids)
                .stream().map(rolePersistenceMapper::entityToDomain)
                .toList();
    }

    @Override
    public List<Role> findAll(int page, int size, String sort) {
        Sort sortOrder = Sort.by(Sort.Direction.ASC, "name");
        Pageable pageable = PageRequest.of(page, size, sortOrder);
        return roleJpaRepository.findAllByIsDeletedFalse(pageable)
                .map(rolePersistenceMapper::entityToDomain)
                .toList();
    }

    @Override
    public boolean existsByName(String name) {
        return roleJpaRepository.existsByNameAndIsDeletedFalse(name);
    }

    @Override
    public long count() {
        return roleJpaRepository.count();
    }

    @Override
    public RolePermission saveRolePermission(RolePermission rolePermission) {
        RolePermissionEntity rolePermissionEntity = rolePersistenceMapper.rolePermissionToEntity(rolePermission);
        RolePermissionEntity saved = rolePermissionJpaRepository.save(rolePermissionEntity);
        return rolePersistenceMapper.entityToRolePermission(saved);
    }

    @Override
    public List<RolePermission> findPermissionsByRoleId(Long roleId) {
        return rolePermissionJpaRepository.findByRoleIdAndRevokedAtIsNull(roleId)
                .stream().map(rolePersistenceMapper::entityToRolePermission).toList();
    }

    @Override
    public Optional<RolePermission> findActiveRolePermission(Long roleId, Long permissionId) {
        return rolePermissionJpaRepository.findByRoleIdAndPermissionIdAndRevokedAtIsNull(roleId, permissionId)
                .map(rolePersistenceMapper::entityToRolePermission);
    }
}
