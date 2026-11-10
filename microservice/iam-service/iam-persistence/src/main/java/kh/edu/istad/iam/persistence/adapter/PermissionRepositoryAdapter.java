package kh.edu.istad.iam.persistence.adapter;

import kh.edu.istad.iam.domain.entity.Permission;
import kh.edu.istad.iam.domain.port.out.PermissionRepository;
import kh.edu.istad.iam.persistence.entity.PermissionEntity;
import kh.edu.istad.iam.persistence.mapper.PermissionPersistenMapper;
import kh.edu.istad.iam.persistence.repository.PermissionJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
@Component
@RequiredArgsConstructor
public class PermissionRepositoryAdapter implements PermissionRepository {

    private final PermissionJpaRepository permissionJpaRepository;
    private final PermissionPersistenMapper permissionPersistenMapper;

    @Override
    public Permission save(Permission permission) {
        PermissionEntity permissionEntity = permissionPersistenMapper.permissionToEntity(permission);
        PermissionEntity permissionEntitySaved = permissionJpaRepository.save(permissionEntity);
        return permissionPersistenMapper.entityToDomain(permissionEntitySaved);
    }

    @Override
    public Optional<Permission> findById(Long id) {
        return permissionJpaRepository.findById(id)
                .filter(permissionEntity -> permissionEntity.isDeleted())
                .map(permissionPersistenMapper::entityToDomain);
    }

    @Override
    public Optional<Permission> findByUuid(UUID uuid) {
        return permissionJpaRepository.findByUuidAndIsDeletedFalse(uuid)
                .map(permissionPersistenMapper::entityToDomain);
    }

    @Override
    public Optional<Permission> findByName(String name) {
        return permissionJpaRepository.findByNameAndIsDeletedFalse(name)
                .map(permissionPersistenMapper::entityToDomain);
    }

    @Override
    public List<Permission> findByUuids(Set<UUID> uuids) {
        return permissionJpaRepository.findByUuidInAndIsDeletedFalse(uuids)
                .stream()
                .map(permissionPersistenMapper::entityToDomain)
                .toList();
    }

    @Override
    public boolean existsByName(String name) {
        return permissionJpaRepository.existsByNameAndIsDeletedFalse(name);
    }

    @Override
    public boolean existsByResourceAndAction(String resource, String action) {
        return permissionJpaRepository.existsByResourceAndActionAndIsDeletedFalse(resource, action);
    }

    @Override
    public List<Permission> findAll(int page, int size, String sort) {
        Sort sortOrder = Sort.by(Sort.Direction.ASC, "name");
        Pageable pageable = PageRequest.of(page, size, sortOrder);
        return permissionJpaRepository.findAllByIsDeletedFalse(pageable)
                .stream().map(permissionPersistenMapper::entityToDomain)
                .toList();

    }

    @Override
    public long count() {
        return permissionJpaRepository.count();
    }
}
