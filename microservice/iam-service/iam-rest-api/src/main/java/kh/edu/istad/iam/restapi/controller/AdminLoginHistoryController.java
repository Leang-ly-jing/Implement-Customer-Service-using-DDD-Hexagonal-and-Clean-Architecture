package kh.edu.istad.iam.restapi.controller;

import kh.edu.istad.iam.restapi.dto.response.LoginHistoryResponse;
import kh.edu.istad.iam.restapi.dto.response.LoginHistoryStatsResponse;
import kh.edu.istad.iam.restapi.dto.response.PageResponse;
import kh.edu.istad.iam.restapi.service.LoginHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/login-history")
@RequiredArgsConstructor
public class AdminLoginHistoryController {

    private final LoginHistoryService loginHistoryService;

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('user:read')")
    public PageResponse<LoginHistoryResponse> getGlobalLoginHistory(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "20") int size
    ) {
        return loginHistoryService.getGlobalLoginHistory(page, size);
    }

    @GetMapping("/stats")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('user:read')")
    public LoginHistoryStatsResponse getStats() {
        return loginHistoryService.getStats();
    }
}
