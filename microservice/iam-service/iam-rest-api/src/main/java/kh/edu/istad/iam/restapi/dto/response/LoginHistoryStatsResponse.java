package kh.edu.istad.iam.restapi.dto.response;

public record LoginHistoryStatsResponse(
        long totalLoginsToday,
        long failedLogins24h,
        long suspiciousLogins24h,
        long totalLogins24h
) {}
