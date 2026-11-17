package kh.edu.istad.iam.restapi.controller;

import kh.edu.istad.iam.restapi.dto.response.AuditLogResponse;
import kh.edu.istad.iam.restapi.dto.response.PageResponse;
import kh.edu.istad.iam.restapi.service.AuditLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/audit-logs")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN') or hasAuthority('ROLE_ADMIN') or hasAuthority('audit:read')")
public class AuditLogController {

    private final AuditLogService service;

    @GetMapping
    public PageResponse<AuditLogResponse> getAll(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "20") int size
    ) {
        return service.findAll(page, size);
    }
}
