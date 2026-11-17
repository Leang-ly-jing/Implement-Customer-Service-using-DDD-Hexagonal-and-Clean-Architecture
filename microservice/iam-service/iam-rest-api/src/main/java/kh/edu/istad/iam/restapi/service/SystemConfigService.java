package kh.edu.istad.iam.restapi.service;

import kh.edu.istad.iam.domain.dto.result.SystemConfigResult;
import kh.edu.istad.iam.domain.valueobject.AuditAction;
import kh.edu.istad.iam.persistence.entity.SystemConfigEntity;
import kh.edu.istad.iam.persistence.repository.SystemConfigJpaRepository;
import kh.edu.istad.iam.restapi.dto.request.UpdateSystemConfigRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Slf4j
@Service
@RequiredArgsConstructor
public class SystemConfigService {

    private static final String CONFIG_ID = "DEFAULT";
    private final SystemConfigJpaRepository configRepository;
    private final AuditLogService auditLogService;

    @Cacheable(value = "system_config", key = "'DEFAULT'", unless = "#result == null")
    @Transactional(readOnly = true)
    public SystemConfigResult get() {
        return configRepository.findById(CONFIG_ID)
                .map(this::toResult)
                .orElseGet(this::initDefaultConfig);
    }

    @CacheEvict(value = "system_config", key = "'DEFAULT'")
    @Transactional
    public SystemConfigResult update(UpdateSystemConfigRequest request, String actor) {
        SystemConfigEntity entity = configRepository.findById(CONFIG_ID)
                .orElseGet(() -> SystemConfigEntity.builder().id(CONFIG_ID).build());

        entity.setPasswordMinLength(request.passwordMinLength());
        entity.setRequireUppercase(request.requireUppercase());
        entity.setRequireDigit(request.requireDigit());
        entity.setRequireSpecial(request.requireSpecial());
        entity.setMaxFailedAttempts(request.maxFailedAttempts());
        entity.setLockDurationMinutes(request.lockDurationMinutes());
        entity.setSessionExpirationMinutes(request.sessionExpirationMinutes());
        entity.setAccessTokenTtlMinutes(request.accessTokenTtlMinutes());
        entity.setRefreshTokenTtlDays(request.refreshTokenTtlDays());
        entity.setUpdatedAt(Instant.now());
        entity.setUpdatedBy(actor != null ? actor : "system");

        SystemConfigEntity saved = configRepository.save(entity);

        auditLogService.record(
                actor != null ? actor : "system",
                AuditAction.CONFIG_UPDATED,
                "SYSTEM_CONFIG",
                CONFIG_ID,
                "Updated system configuration: minLen=" + request.passwordMinLength()
                        + ", maxAttempts=" + request.maxFailedAttempts()
                        + ", lockDuration=" + request.lockDurationMinutes() + "m"
                        + ", accessTtl=" + request.accessTokenTtlMinutes() + "m",
                null
        );

        return toResult(saved);
    }

    private synchronized SystemConfigResult initDefaultConfig() {
        return configRepository.findById(CONFIG_ID)
                .map(this::toResult)
                .orElseGet(() -> {
                    SystemConfigEntity entity = SystemConfigEntity.builder()
                            .id(CONFIG_ID)
                            .passwordMinLength(8)
                            .requireUppercase(true)
                            .requireDigit(true)
                            .requireSpecial(true)
                            .maxFailedAttempts(5)
                            .lockDurationMinutes(15)
                            .sessionExpirationMinutes(1440)
                            .accessTokenTtlMinutes(60)
                            .refreshTokenTtlDays(30)
                            .updatedAt(Instant.now())
                            .updatedBy("SYSTEM")
                            .build();
                    return toResult(configRepository.save(entity));
                });
    }

    private SystemConfigResult toResult(SystemConfigEntity entity) {
        return new SystemConfigResult(
                entity.getPasswordMinLength(),
                entity.isRequireUppercase(),
                entity.isRequireDigit(),
                entity.isRequireSpecial(),
                entity.getMaxFailedAttempts(),
                entity.getLockDurationMinutes(),
                entity.getSessionExpirationMinutes(),
                entity.getAccessTokenTtlMinutes(),
                entity.getRefreshTokenTtlDays()
        );
    }
}
