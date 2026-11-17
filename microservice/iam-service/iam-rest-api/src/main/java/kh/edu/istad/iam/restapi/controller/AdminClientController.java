package kh.edu.istad.iam.restapi.controller;

import jakarta.validation.Valid;
import kh.edu.istad.iam.restapi.dto.request.CreateClientRequest;
import kh.edu.istad.iam.restapi.dto.request.UpdateClientRequest;
import kh.edu.istad.iam.restapi.dto.response.*;
import kh.edu.istad.iam.restapi.service.ClientManagementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/clients")
@RequiredArgsConstructor
public class AdminClientController {

    private final ClientManagementService clientManagementService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('ROLE_ADMIN') or hasAuthority('client:create')")
    public CreateClientResponse registerClient(@Valid @RequestBody CreateClientRequest request) {
        return clientManagementService.registerClient(request);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('ROLE_ADMIN') or hasAuthority('client:read')")
    public PageResponse<ClientSummaryResponse> listClients(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            @RequestParam(name = "search", required = false) String search
    ) {
        return clientManagementService.listClients(page, size, search);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('ROLE_ADMIN') or hasAuthority('client:read')")
    public ClientDetailsResponse getClientById(@PathVariable("id") String id) {
        return clientManagementService.getClientById(id);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('ROLE_ADMIN') or hasAuthority('client:update')")
    public ClientDetailsResponse updateClient(
            @PathVariable("id") String id,
            @Valid @RequestBody UpdateClientRequest request
    ) {
        return clientManagementService.updateClient(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('ROLE_ADMIN') or hasAuthority('client:delete')")
    public void deleteClient(@PathVariable("id") String id) {
        clientManagementService.deleteClient(id);
    }

    @PostMapping("/{id}/regenerate-secret")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('ROLE_ADMIN') or hasAuthority('client:update')")
    public RegenerateSecretResponse regenerateSecret(@PathVariable("id") String id) {
        return clientManagementService.regenerateSecret(id);
    }
}
