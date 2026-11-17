package kh.edu.istad.iam.restapi.service;

import kh.edu.istad.iam.domain.valueobject.AuditAction;
import kh.edu.istad.iam.persistence.entity.AuditLogEntity;
import kh.edu.istad.iam.persistence.repository.AuditLogJpaRepository;
import kh.edu.istad.iam.restapi.dto.response.AuditLogResponse;
import kh.edu.istad.iam.restapi.dto.response.PageResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuditLogService {

    private final AuditLogJpaRepository repository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void record(AuditAction action, String targetType, String targetId, String details) {
        String actor = Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
                .map(Authentication::getName)
                .filter(name -> !name.isBlank() && !"anonymousUser".equalsIgnoreCase(name))
                .orElse("system");
        save(actor, action, targetType, targetId, details, null);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void record(String actor, AuditAction action, String targetType,
                       String targetId, String details, String ip) {
        save(actor, action, targetType, targetId, details, ip);
    }

    private void save(String actor, AuditAction action, String targetType,
                      String targetId, String details, String ip) {
        try {
            AuditLogEntity logEntry = AuditLogEntity.builder()
                    .actor(actor != null ? actor : "system")
                    .action(action)
                    .targetType(targetType)
                    .targetId(targetId)
                    .details(details)
                    .ipAddress(ip)
                    .createdAt(Instant.now())
                    .build();
            repository.save(logEntry);
        } catch (Exception ex) {
            log.warn("Failed to write audit log entry for action {}: {}", action, ex.getMessage());
        }
    }

    @Transactional(readOnly = true)
    public PageResponse<AuditLogResponse> findAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<AuditLogEntity> resultPage = repository.findAll(pageable);

        List<AuditLogResponse> dtoList = resultPage.getContent().stream()
                .map(this::toResponse)
                .toList();

        return new PageResponse<>(
                dtoList,
                new PageResponse.PageMetadata(
                        resultPage.getNumber(),
                        resultPage.getSize(),
                        resultPage.getTotalElements(),
                        resultPage.getTotalPages()
                )
        );
    }

    private AuditLogResponse toResponse(AuditLogEntity e) {
        return new AuditLogResponse(
                e.getId(),
                e.getActor(),
                e.getAction(),
                e.getTargetType(),
                e.getTargetId(),
                e.getDetails(),
                e.getIpAddress(),
                e.getCreatedAt()
        );
    }
}
