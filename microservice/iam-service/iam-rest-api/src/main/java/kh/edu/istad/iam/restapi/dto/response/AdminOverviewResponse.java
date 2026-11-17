package kh.edu.istad.iam.restapi.dto.response;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record AdminOverviewResponse(
        long totalUsers,
        long activeUsers,
        long totalRoles,
        long totalPermissions,
        long totalClients,
        List<RecentActivityItem> recentActivities
) {
    public record RecentActivityItem(
            Long id,
            UUID userUuid,
            String action,
            String metadata,
            String ipAddress,
            String device,
            Instant createdAt
    ) {}
}
