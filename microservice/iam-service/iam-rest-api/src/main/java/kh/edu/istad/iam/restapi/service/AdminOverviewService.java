package kh.edu.istad.iam.restapi.service;

import kh.edu.istad.iam.persistence.repository.AccountActivityJpaRepository;
import kh.edu.istad.iam.persistence.repository.PermissionJpaRepository;
import kh.edu.istad.iam.persistence.repository.RoleJpaRepository;
import kh.edu.istad.iam.persistence.repository.UserJpaRepository;
import kh.edu.istad.iam.restapi.dto.response.AdminOverviewResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminOverviewService {

    private final UserJpaRepository userJpaRepository;
    private final RoleJpaRepository roleJpaRepository;
    private final PermissionJpaRepository permissionJpaRepository;
    private final AccountActivityJpaRepository accountActivityJpaRepository;
    private final JdbcTemplate jdbcTemplate;

    @Transactional(readOnly = true)
    public AdminOverviewResponse getOverview() {
        long totalUsers = userJpaRepository.countByIsDeletedFalse();
        long activeUsers = userJpaRepository.countByEnabledTrueAndIsDeletedFalse();
        long totalRoles = roleJpaRepository.countByIsDeletedFalse();
        long totalPermissions = permissionJpaRepository.countByIsDeletedFalse();

        long totalClients = 0;
        try {
            Long count = jdbcTemplate.queryForObject("SELECT count(*) FROM oauth2_registered_client", Long.class);
            totalClients = count != null ? count : 0;
        } catch (Exception ignored) {
        }

        List<AdminOverviewResponse.RecentActivityItem> recentActivities = accountActivityJpaRepository
                .findRecentActivities(PageRequest.of(0, 10)).stream()
                .map(a -> new AdminOverviewResponse.RecentActivityItem(
                        a.getId(),
                        a.getUserUuid(),
                        a.getAction(),
                        a.getMetadata(),
                        a.getIpAddress(),
                        a.getDevice(),
                        a.getCreatedAt()
                ))
                .toList();

        return new AdminOverviewResponse(
                totalUsers,
                activeUsers,
                totalRoles,
                totalPermissions,
                totalClients,
                recentActivities
        );
    }
}
