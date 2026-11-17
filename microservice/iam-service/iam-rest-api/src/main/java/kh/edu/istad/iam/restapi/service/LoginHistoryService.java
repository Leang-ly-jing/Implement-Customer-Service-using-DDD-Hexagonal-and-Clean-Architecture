package kh.edu.istad.iam.restapi.service;

import jakarta.servlet.http.HttpServletRequest;
import kh.edu.istad.iam.domain.valueobject.AuditAction;
import kh.edu.istad.iam.domain.valueobject.LoginFailureReason;
import kh.edu.istad.iam.domain.valueobject.LoginStatus;
import kh.edu.istad.iam.persistence.entity.LoginHistoryEntity;
import kh.edu.istad.iam.persistence.entity.UserEntity;
import kh.edu.istad.iam.persistence.repository.LoginHistoryJpaRepository;
import kh.edu.istad.iam.persistence.repository.UserJpaRepository;
import kh.edu.istad.iam.restapi.config.LoginMonitoringProperties;
import kh.edu.istad.iam.restapi.dto.response.LoginHistoryResponse;
import kh.edu.istad.iam.restapi.dto.response.LoginHistoryStatsResponse;
import kh.edu.istad.iam.restapi.dto.response.PageResponse;
import kh.edu.istad.iam.restapi.util.UserAgentParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoginHistoryService {

    private final LoginHistoryJpaRepository loginHistoryRepository;
    private final UserJpaRepository userRepository;
    private final UserAgentParser userAgentParser;
    private final LoginMonitoringProperties monitoringProperties;
    private final AuditLogService auditLogService;

    @Transactional
    public void recordLoginSuccess(String usernameOrEmail, HttpServletRequest request) {
        String normalized = usernameOrEmail != null ? usernameOrEmail.trim().toLowerCase() : "unknown";
        UUID userUuid = userRepository.findByUsernameOrEmail(normalized)
                .map(UserEntity::getUuid)
                .orElse(null);

        record(userUuid, normalized, LoginStatus.SUCCESS, null, request);
    }

    @Transactional
    public void recordLoginFailure(String usernameOrEmail, LoginFailureReason reason, HttpServletRequest request) {
        String normalized = usernameOrEmail != null ? usernameOrEmail.trim().toLowerCase() : "unknown";
        UUID userUuid = null;
        try {
            userUuid = userRepository.findByUsernameOrEmail(normalized)
                    .map(UserEntity::getUuid)
                    .orElse(null);
        } catch (Exception ignored) {
        }

        record(userUuid, normalized, LoginStatus.FAILED, reason != null ? reason : LoginFailureReason.UNKNOWN, request);
    }

    @Transactional
    public void recordLoginFailure(String usernameOrEmail, String reasonStr, HttpServletRequest request) {
        LoginFailureReason reason = parseReasonString(reasonStr);
        recordLoginFailure(usernameOrEmail, reason, request);
    }

    private void record(UUID userUuid, String username, LoginStatus status, LoginFailureReason reason, HttpServletRequest request) {
        String userAgent = request != null ? request.getHeader("User-Agent") : null;
        String ip = resolveClientIp(request);
        UserAgentParser.DeviceInfo deviceInfo = userAgentParser.parse(userAgent);

        boolean isSuspicious = false;
        String suspiciousReason = null;

        if (status == LoginStatus.FAILED) {
            Instant now = Instant.now();

            if (reason == LoginFailureReason.ACCOUNT_LOCKED) {
                isSuspicious = true;
                suspiciousReason = "Attempt to log in to locked account";
            }

            if (ip != null && !ip.isBlank()) {
                Instant ipWindow = now.minus(monitoringProperties.getIpFloodWindowMinutes(), ChronoUnit.MINUTES);
                long distinctUsers = loginHistoryRepository.countDistinctFailedUsernamesByIpSince(ip, "FAILED", ipWindow);
                if (distinctUsers >= monitoringProperties.getIpFloodDistinctUsernames()) {
                    isSuspicious = true;
                    suspiciousReason = "IP flooding attack detected: " + (distinctUsers + 1) + " distinct accounts targeted";
                }
            }

            if (!isSuspicious && username != null && !username.isBlank()) {
                Instant bruteForceWindow = now.minus(monitoringProperties.getBruteForceWindowMinutes(), ChronoUnit.MINUTES);
                long failedCount = loginHistoryRepository.countFailedAttemptsByUsernameSince(username, "FAILED", bruteForceWindow);
                if (failedCount >= monitoringProperties.getBruteForceAttempts()) {
                    isSuspicious = true;
                    suspiciousReason = "Repeated failed attempts (possible brute force: " + (failedCount + 1) + " attempts)";
                }
            }
        }

        LoginHistoryEntity entry = LoginHistoryEntity.builder()
                .userUuid(userUuid)
                .username(username)
                .status(status != null ? status.name() : "FAILED")
                .failureReason(reason != null ? reason.name() : null)
                .ipAddress(ip)
                .device(deviceInfo.browserAndOs())
                .deviceType(deviceInfo.deviceType())
                .isSuspicious(isSuspicious)
                .suspiciousReason(suspiciousReason)
                .loginAt(Instant.now())
                .build();

        loginHistoryRepository.save(entry);

        if (status == LoginStatus.SUCCESS) {
            auditLogService.record(
                    username,
                    AuditAction.LOGIN_SUCCESS,
                    "USER",
                    userUuid != null ? userUuid.toString() : username,
                    "Login successful from " + deviceInfo.browserAndOs(),
                    ip
            );
        } else {
            auditLogService.record(
                    username,
                    AuditAction.LOGIN_FAILURE,
                    "USER",
                    userUuid != null ? userUuid.toString() : username,
                    "Login failed (" + (reason != null ? reason.name() : "UNKNOWN") + ") from " + deviceInfo.browserAndOs(),
                    ip
            );
        }
    }

    @Transactional(readOnly = true)
    public List<LoginHistoryResponse> getLoginHistory(UUID userUuid) {
        return loginHistoryRepository.findTop50ByUserUuidOrderByLoginAtDesc(userUuid).stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public PageResponse<LoginHistoryResponse> getUserLoginHistory(UUID userUuid, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "loginAt"));
        Page<LoginHistoryEntity> pageResult = loginHistoryRepository.findByUserUuidOrderByLoginAtDesc(userUuid, pageable);

        return toPageResponse(pageResult);
    }

    @Transactional(readOnly = true)
    public PageResponse<LoginHistoryResponse> getGlobalLoginHistory(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "loginAt"));
        Page<LoginHistoryEntity> pageResult = loginHistoryRepository.findAllByOrderByLoginAtDesc(pageable);

        return toPageResponse(pageResult);
    }

    @Transactional(readOnly = true)
    public LoginHistoryStatsResponse getStats() {
        Instant now = Instant.now();
        Instant todayStart = now.truncatedTo(ChronoUnit.DAYS);
        Instant last24h = now.minus(24, ChronoUnit.HOURS);

        long totalLoginsToday = loginHistoryRepository.countByLoginAtGreaterThanEqual(todayStart);
        long totalLogins24h = loginHistoryRepository.countByLoginAtGreaterThanEqual(last24h);
        long failedLogins24h = loginHistoryRepository.countByStatusAndLoginAtGreaterThanEqual("FAILED", last24h);
        long suspiciousLogins24h = loginHistoryRepository.countByIsSuspiciousTrueAndLoginAtGreaterThanEqual(last24h);

        return new LoginHistoryStatsResponse(
                totalLoginsToday,
                failedLogins24h,
                suspiciousLogins24h,
                totalLogins24h
        );
    }

    @Scheduled(cron = "0 0 3 * * ?")
    @Transactional
    public void scheduledRetentionCleanup() {
        Instant cutoff = Instant.now().minus(monitoringProperties.getRetentionDays(), ChronoUnit.DAYS);
        log.info("Starting login history retention cleanup for records older than {}", cutoff);
        int deletedTotal = 0;
        int batchSize = 1000;
        int deleted;
        do {
            try {
                deleted = loginHistoryRepository.deleteOldBatch(cutoff, batchSize);
                deletedTotal += deleted;
            } catch (Exception ex) {
                log.warn("Batch deletion encountered error: {}", ex.getMessage());
                break;
            }
        } while (deleted == batchSize);
        log.info("Completed login history retention cleanup. Removed {} records.", deletedTotal);
    }

    private LoginHistoryResponse toResponse(LoginHistoryEntity h) {
        return new LoginHistoryResponse(
                h.getId(),
                h.getUserUuid(),
                h.getUsername(),
                "SUCCESS".equalsIgnoreCase(h.getStatus()),
                h.getStatus(),
                normalizeFailureReason(h.getFailureReason()),
                h.getIpAddress(),
                h.getDevice(),
                h.getDeviceType(),
                h.isSuspicious(),
                h.getSuspiciousReason(),
                h.getLoginAt()
        );
    }

    private String normalizeFailureReason(String raw) {
        if (raw == null || raw.isBlank()) return null;
        String upper = raw.trim().toUpperCase();
        if (upper.contains("DISABLED")) return "ACCOUNT_DISABLED";
        if (upper.contains("LOCKED")) return "ACCOUNT_LOCKED";
        if (upper.contains("CREDENTIAL") || upper.contains("PASSWORD")) return "BAD_CREDENTIALS";
        if (upper.contains("NOT_FOUND") || upper.contains("NOT FOUND")) return "USER_NOT_FOUND";
        return upper;
    }

    private PageResponse<LoginHistoryResponse> toPageResponse(Page<LoginHistoryEntity> pageResult) {
        List<LoginHistoryResponse> content = pageResult.getContent().stream()
                .map(this::toResponse)
                .toList();

        return new PageResponse<>(
                content,
                new PageResponse.PageMetadata(
                        pageResult.getNumber(),
                        pageResult.getSize(),
                        pageResult.getTotalElements(),
                        pageResult.getTotalPages()
                )
        );
    }

    private LoginFailureReason parseReasonString(String reasonStr) {
        if (reasonStr == null) return LoginFailureReason.UNKNOWN;
        String lower = reasonStr.toLowerCase();
        if (lower.contains("bad credential") || lower.contains("password")) {
            return LoginFailureReason.BAD_CREDENTIALS;
        } else if (lower.contains("locked")) {
            return LoginFailureReason.ACCOUNT_LOCKED;
        } else if (lower.contains("disabled")) {
            return LoginFailureReason.ACCOUNT_DISABLED;
        } else if (lower.contains("not found")) {
            return LoginFailureReason.USER_NOT_FOUND;
        }
        return LoginFailureReason.UNKNOWN;
    }

    private String resolveClientIp(HttpServletRequest request) {
        if (request == null) return null;
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isBlank()) {
            return xForwardedFor.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }
}
