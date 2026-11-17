package kh.edu.istad.iam.restapi.controller;

import kh.edu.istad.iam.restapi.dto.response.AdminOverviewResponse;
import kh.edu.istad.iam.restapi.service.AdminOverviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/overview")
@RequiredArgsConstructor
public class AdminOverviewController {

    private final AdminOverviewService adminOverviewService;

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('user:read')")
    public AdminOverviewResponse getOverview() {
        return adminOverviewService.getOverview();
    }
}
