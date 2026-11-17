package kh.edu.istad.iam.restapi.service;

import jakarta.servlet.http.HttpServletRequest;
import kh.edu.istad.iam.persistence.entity.UserEntity;
import kh.edu.istad.iam.persistence.entity.UserSessionEntity;
import kh.edu.istad.iam.persistence.repository.UserJpaRepository;
import kh.edu.istad.iam.persistence.repository.UserSessionJpaRepository;
import kh.edu.istad.iam.restapi.dto.response.UserSessionResponse;
import kh.edu.istad.iam.restapi.util.UserAgentParser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserSessionService {

    private final UserSessionJpaRepository sessionRepository;
    private final UserJpaRepository userRepository;
    private final UserAgentParser userAgentParser;
    private final SystemConfigService configService;

    @Transactional
    public UUID createSession(String usernameOrEmail, HttpServletRequest request) {
        UserEntity user = userRepository.findByUsernameOrEmail(usernameOrEmail)
                .orElse(null);

        if (user == null) {
            return null;
        }

        String userAgent = request != null ? request.getHeader("User-Agent") : null;
        String ip = request != null ? request.getRemoteAddr() : null;
        UserAgentParser.DeviceInfo deviceInfo = userAgentParser.parse(userAgent);

        var config = configService.get();
        int expMinutes = (config != null && config.sessionExpirationMinutes() > 0)
                ? config.sessionExpirationMinutes()
                : 1440;
        Instant expiresAt = Instant.now().plus(java.time.Duration.ofMinutes(expMinutes));

        UserSessionEntity session = UserSessionEntity.builder()
                .userUuid(user.getUuid())
                .deviceType(deviceInfo.deviceType())
                .browser(deviceInfo.browserAndOs())
                .ipAddress(ip)
                .location(null)
                .expiresAt(expiresAt)
                .lastActiveAt(Instant.now())
                .createdAt(Instant.now())
                .build();

        return sessionRepository.save(session).getId();
    }

    @Transactional(readOnly = true)
    public List<UserSessionResponse> getSessions(UUID userUuid, UUID currentSid) {
        return sessionRepository.findAllByUserUuidAndRevokedAtIsNullOrderByLastActiveAtDesc(userUuid).stream()
                .map(s -> new UserSessionResponse(
                        s.getId(),
                        s.getDeviceType(),
                        s.getBrowser(),
                        s.getIpAddress(),
                        s.getLocation(),
                        s.getLastActiveAt(),
                        s.getCreatedAt(),
                        currentSid != null && s.getId().equals(currentSid)
                ))
                .toList();
    }

    @Transactional
    public void revokeSession(UUID userUuid, UUID sessionId) {
        sessionRepository.revokeSession(sessionId, userUuid, Instant.now());
    }

    @Transactional
    public void revokeAllOtherSessions(UUID userUuid, UUID currentSid) {
        if (currentSid == null) {
            throw new IllegalArgumentException("Current session id is required to revoke other sessions");
        }
        sessionRepository.revokeOtherSessions(userUuid, currentSid, Instant.now());
    }

    @Transactional
    public void revokeAllSessions(UUID userUuid) {
        sessionRepository.revokeAllSessions(userUuid, Instant.now());
    }

    @Transactional(readOnly = true)
    public List<UserSessionResponse> getSessionsForUser(UUID userUuid) {
        return sessionRepository.findAllByUserUuidAndRevokedAtIsNullOrderByLastActiveAtDesc(userUuid).stream()
                .map(s -> new UserSessionResponse(
                        s.getId(),
                        s.getDeviceType(),
                        s.getBrowser(),
                        s.getIpAddress(),
                        s.getLocation(),
                        s.getLastActiveAt(),
                        s.getCreatedAt(),
                        false
                ))
                .toList();
    }

    @Transactional
    public void adminRevokeSession(UUID userUuid, UUID sessionId) {
        sessionRepository.revokeSession(sessionId, userUuid, Instant.now());
    }

    @Transactional
    public void adminRevokeAllSessions(UUID userUuid) {
        sessionRepository.revokeAllSessions(userUuid, Instant.now());
    }

    @Transactional
    public void revokeAllForUser(UUID userUuid) {
        adminRevokeAllSessions(userUuid);
    }

    @Transactional
    public void revokeAllByUser(String usernameOrEmail) {
        if (usernameOrEmail == null || usernameOrEmail.isBlank()) return;
        userRepository.findByUsernameOrEmail(usernameOrEmail.trim().toLowerCase())
                .ifPresent(u -> adminRevokeAllSessions(u.getUuid()));
    }
}
