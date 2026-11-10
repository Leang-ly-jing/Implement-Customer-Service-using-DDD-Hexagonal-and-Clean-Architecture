package kh.edu.istad.iam.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.Collections;
import java.util.Set;

@Controller
@RequiredArgsConstructor
public class ConsentController {

    private final RegisteredClientRepository registeredClientRepository;

    @GetMapping("/oauth2/consent")
    public String consent(
            Principal principal,
            Model model,
            @RequestParam("client_id") String clientId,
            @RequestParam(value = "scope", required = false, defaultValue = "") String scope,
            @RequestParam(value = "state", required = false, defaultValue = "") String state) {

        RegisteredClient registeredClient = this.registeredClientRepository.findByClientId(clientId);
        String clientName = clientId;
        if (registeredClient != null && StringUtils.hasText(registeredClient.getClientName())) {
            String name = registeredClient.getClientName().trim();
            if (!name.matches("^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$")) {
                clientName = name;
            }
        }
        if ("oidc-client".equalsIgnoreCase(clientName)) {
            clientName = "OIDC Client";
        }

        Set<String> scopesToApprove = StringUtils.hasText(scope)
                ? Set.of(scope.trim().split("\\s+"))
                : Collections.emptySet();

        model.addAttribute("clientId", clientId);
        model.addAttribute("clientName", clientName);
        model.addAttribute("state", state);
        model.addAttribute("scopes", scopesToApprove);

        return "consent";
    }
}

