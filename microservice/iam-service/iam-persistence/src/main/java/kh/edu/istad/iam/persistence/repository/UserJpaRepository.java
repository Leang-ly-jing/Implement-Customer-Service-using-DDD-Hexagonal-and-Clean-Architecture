package kh.edu.istad.iam.persistence.repository;

import kh.edu.istad.iam.persistence.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface UserJpaRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByUuidAndIsDeletedFalse(UUID uuid);

    Optional<UserEntity> findByUsernameAndIsDeletedFalse(String username);

    Optional<UserEntity> findByEmailAndIsDeletedFalse(String email);

    @Query("SELECT u FROM UserEntity u WHERE (u.username = :identifier OR u.email = :identifier) AND u.isDeleted = false")
    Optional<UserEntity> findByUsernameOrEmail(@Param("identifier") String identifier);

    boolean existsByUsernameAndIsDeletedFalse(String username);

    boolean existsByEmailAndIsDeletedFalse(String email);

    boolean existsByPhoneNumberAndIsDeletedFalse(String phoneNumber);

    Page<UserEntity> findAllByIsDeletedFalse(Pageable pageable);

    long countByIsDeletedFalse();
}