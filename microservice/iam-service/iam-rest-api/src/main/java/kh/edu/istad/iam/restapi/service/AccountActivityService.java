package kh.edu.istad.iam.restapi.service;

import kh.edu.istad.iam.domain.entity.AccountActivity;
import kh.edu.istad.iam.domain.port.out.AccountActivityRepository;
import kh.edu.istad.iam.domain.valueobject.AccountAction;
import kh.edu.istad.iam.restapi.dto.request.ClientInfo;
import kh.edu.istad.iam.restapi.dto.response.AccountActivityResponse;
import kh.edu.istad.iam.restapi.util.UserAgentParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountActivityService {

    private final AccountActivityRepository accountActivityRepository;
    private final UserAgentParser userAgentParser;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void record(UUID userUuid, AccountAction action, String metadata, ClientInfo client) {
        try {
            String device = client != null ? userAgentParser.parse(client.userAgent()).browserAndOs() : "Unknown Device";
            String ip = client != null ? client.ip() : null;

            AccountActivity activity = AccountActivity.builder()
                    .userUuid(userUuid)
                    .action(action)
                    .metadata(metadata)
                    .ipAddress(ip)
                    .device(device)
                    .createdAt(Instant.now())
                    .build();

            accountActivityRepository.save(activity);
        } catch (Exception e) {
            log.error("Failed to record account activity: {}", e.getMessage(), e);
        }
    }

    @Transactional(readOnly = true)
    public List<AccountActivityResponse> getActivities(UUID userUuid) {
        return accountActivityRepository.findRecent(userUuid, 50).stream()
                .map(a -> new AccountActivityResponse(
                        a.getId(),
                        a.getAction().name(),
                        a.getMetadata(),
                        a.getIpAddress(),
                        a.getDevice(),
                        a.getCreatedAt()
                ))
                .toList();
    }
}
