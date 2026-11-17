package kh.edu.istad.iam.persistence.adapter;

import kh.edu.istad.iam.domain.entity.AccountActivity;
import kh.edu.istad.iam.domain.port.out.AccountActivityRepository;
import kh.edu.istad.iam.domain.valueobject.AccountAction;
import kh.edu.istad.iam.persistence.entity.AccountActivityEntity;
import kh.edu.istad.iam.persistence.repository.AccountActivityJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class AccountActivityRepositoryAdapter implements AccountActivityRepository {

    private final AccountActivityJpaRepository accountActivityJpaRepository;

    @Override
    public void save(AccountActivity activity) {
        AccountActivityEntity entity = AccountActivityEntity.builder()
                .userUuid(activity.getUserUuid())
                .action(activity.getAction().name())
                .metadata(activity.getMetadata())
                .ipAddress(activity.getIpAddress())
                .device(activity.getDevice())
                .createdAt(activity.getCreatedAt())
                .build();
        accountActivityJpaRepository.save(entity);
    }

    @Override
    public List<AccountActivity> findRecent(UUID userUuid, int limit) {
        return accountActivityJpaRepository.findAllByUserUuidOrderByCreatedAtDesc(userUuid, PageRequest.of(0, limit)).stream()
                .map(this::toDomain)
                .toList();
    }

    private AccountActivity toDomain(AccountActivityEntity entity) {
        AccountAction action;
        try {
            action = AccountAction.valueOf(entity.getAction());
        } catch (Exception e) {
            action = AccountAction.PROFILE_UPDATED;
        }

        return AccountActivity.builder()
                .id(entity.getId())
                .userUuid(entity.getUserUuid())
                .action(action)
                .metadata(entity.getMetadata())
                .ipAddress(entity.getIpAddress())
                .device(entity.getDevice())
                .createdAt(entity.getCreatedAt())
                .build();
    }
}
