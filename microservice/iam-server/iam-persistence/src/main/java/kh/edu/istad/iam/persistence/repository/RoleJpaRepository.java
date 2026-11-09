package kh.edu.istad.iam.persistence.repository;

import kh.edu.istad.iam.persistence.entity.RoleEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RoleJpaRepository extends JpaRepository<RoleEntity, Long> {

    Optional<RoleEntity> findByUuidAndIsDeletedFalse(UUID uuid);
    Optional<RoleEntity> findByNameAndIsDeletedFalse(String name);
    List<RoleEntity> findByUuidInAndIsDeletedFalse(Collection<UUID> uuids);
    boolean existsByNameAndIsDeletedFalse(String name);
    Page<RoleEntity> findAllByIsDeletedFalse(Pageable pageable);
    long countByIsDeletedFalse();
}
