package kh.edu.istad.iam.persistence.repository;

import kh.edu.istad.iam.persistence.entity.PermissionEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PermissionJpaRepository extends JpaRepository<PermissionEntity, Long> {

    Optional<PermissionEntity> findByUuidAndIsDeletedFalse(UUID uuid);
    Optional<PermissionEntity> findByNameAndIsDeletedFalse(String name);

    List<PermissionEntity> findByUuidInAndIsDeletedFalse(Collection<UUID> uuids);

    boolean existsByNameAndIsDeletedFalse(String name);
    boolean existsByResourceAndActionAndIsDeletedFalse(String resource, String action);

    Page<PermissionEntity> findAllByIsDeletedFalse(Pageable pageable);

    long countByIsDeletedFalse();
}