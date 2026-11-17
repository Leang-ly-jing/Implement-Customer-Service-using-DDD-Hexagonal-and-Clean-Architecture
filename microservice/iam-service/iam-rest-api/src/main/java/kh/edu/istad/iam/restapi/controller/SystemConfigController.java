package kh.edu.istad.iam.restapi.controller;

import jakarta.validation.Valid;
import kh.edu.istad.iam.domain.dto.result.SystemConfigResult;
import kh.edu.istad.iam.restapi.dto.request.UpdateSystemConfigRequest;
import kh.edu.istad.iam.restapi.service.SystemConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/config")
@RequiredArgsConstructor
public class SystemConfigController {

    private final SystemConfigService configService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('ROLE_ADMIN') or hasAuthority('config:read')")
    public SystemConfigResult getConfig() {
        return configService.get();
    }

    @PutMapping
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('ROLE_ADMIN') or hasAuthority('config:update')")
    public SystemConfigResult updateConfig(@Valid @RequestBody UpdateSystemConfigRequest request,
                                           Authentication authentication) {
        String actor = (authentication != null && authentication.getName() != null)
                ? authentication.getName()
                : "system";
        return configService.update(request, actor);
    }
}
