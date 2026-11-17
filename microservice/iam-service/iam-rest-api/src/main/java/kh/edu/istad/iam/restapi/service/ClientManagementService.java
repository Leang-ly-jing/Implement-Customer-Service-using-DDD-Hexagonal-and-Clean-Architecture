package kh.edu.istad.iam.restapi.service;

import kh.edu.istad.iam.restapi.dto.request.CreateClientRequest;
import kh.edu.istad.iam.restapi.dto.request.UpdateClientRequest;
import kh.edu.istad.iam.restapi.dto.response.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.security.SecureRandom;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClientManagementService {

    private final RegisteredClientRepository registeredClientRepository;
    private final JdbcTemplate jdbcTemplate;
    private final PasswordEncoder passwordEncoder;

    private static final SecureRandom RANDOM = new SecureRandom();
    private static final String SYSTEM_REGISTRAR = "registrar-client";

    @Transactional
    public CreateClientResponse registerClient(CreateClientRequest request) {
        String normalizedClientId = request.clientId().trim().toLowerCase();

        if (registeredClientRepository.findByClientId(normalizedClientId) != null) {
            throw new IllegalArgumentException("Client with ID '" + normalizedClientId + "' already exists");
        }

        String id = UUID.randomUUID().toString();
        Instant issuedAt = Instant.now();

        boolean isPublicOnly = request.clientAuthenticationMethods().stream()
                .allMatch(m -> "none".equalsIgnoreCase(m.trim()));

        String rawSecret = null;
        String encodedSecret = null;

        if (!isPublicOnly) {
            if (StringUtils.hasText(request.clientSecret())) {
                rawSecret = request.clientSecret().trim();
            } else {
                rawSecret = generateSecureSecret();
            }
            encodedSecret = passwordEncoder.encode(rawSecret);
        }

        long accessTtlMinutes = request.accessTokenTimeToLiveMinutes() != null && request.accessTokenTimeToLiveMinutes() > 0
                ? request.accessTokenTimeToLiveMinutes() : 300;
        long refreshTtlDays = request.refreshTokenTimeToLiveDays() != null && request.refreshTokenTimeToLiveDays() > 0
                ? request.refreshTokenTimeToLiveDays() : 7;

        TokenSettings tokenSettings = TokenSettings.builder()
                .accessTokenFormat(OAuth2TokenFormat.SELF_CONTAINED)
                .accessTokenTimeToLive(Duration.ofMinutes(accessTtlMinutes))
                .refreshTokenTimeToLive(Duration.ofDays(refreshTtlDays))
                .authorizationCodeTimeToLive(Duration.ofMinutes(5))
                .reuseRefreshTokens(false)
                .build();

        ClientSettings clientSettings = ClientSettings.builder()
                .requireProofKey(Boolean.TRUE.equals(request.requireProofKey()))
                .requireAuthorizationConsent(Boolean.TRUE.equals(request.requireAuthorizationConsent()))
                .build();

        RegisteredClient.Builder builder = RegisteredClient.withId(id)
                .clientId(normalizedClientId)
                .clientIdIssuedAt(issuedAt)
                .clientName(request.clientName().trim())
                .clientSettings(clientSettings)
                .tokenSettings(tokenSettings);

        if (encodedSecret != null) {
            builder.clientSecret(encodedSecret);
        }

        for (String m : request.clientAuthenticationMethods()) {
            builder.clientAuthenticationMethod(resolveAuthenticationMethod(m));
        }

        for (String g : request.authorizationGrantTypes()) {
            builder.authorizationGrantType(resolveGrantType(g));
        }

        if (request.redirectUris() != null) {
            request.redirectUris().stream()
                    .filter(StringUtils::hasText)
                    .map(String::trim)
                    .forEach(builder::redirectUri);
        }

        if (request.postLogoutRedirectUris() != null) {
            request.postLogoutRedirectUris().stream()
                    .filter(StringUtils::hasText)
                    .map(String::trim)
                    .forEach(builder::postLogoutRedirectUri);
        }

        if (request.scopes() != null) {
            request.scopes().stream()
                    .filter(StringUtils::hasText)
                    .map(s -> s.trim().toLowerCase())
                    .forEach(builder::scope);
        }

        RegisteredClient registered = builder.build();
        registeredClientRepository.save(registered);

        log.info("Registered new OAuth2 client: {} (id: {})", normalizedClientId, id);

        return new CreateClientResponse(
                registered.getId(),
                registered.getClientId(),
                registered.getClientName(),
                rawSecret,
                registered.getClientIdIssuedAt(),
                registered.getClientAuthenticationMethods().stream().map(ClientAuthenticationMethod::getValue).collect(Collectors.toSet()),
                registered.getAuthorizationGrantTypes().stream().map(AuthorizationGrantType::getValue).collect(Collectors.toSet()),
                registered.getRedirectUris(),
                registered.getScopes()
        );
    }

    @Transactional(readOnly = true)
    public PageResponse<ClientSummaryResponse> listClients(int page, int size, String search) {
        StringBuilder sql = new StringBuilder("FROM oauth2_registered_client WHERE 1=1 ");
        List<Object> params = new ArrayList<>();

        if (StringUtils.hasText(search)) {
            sql.append("AND (LOWER(client_id) LIKE ? OR LOWER(client_name) LIKE ?) ");
            String term = "%" + search.trim().toLowerCase() + "%";
            params.add(term);
            params.add(term);
        }

        Long total = jdbcTemplate.queryForObject("SELECT COUNT(*) " + sql, Long.class, params.toArray());
        long totalElements = total != null ? total : 0;
        int totalPages = (int) Math.ceil((double) totalElements / size);

        String selectSql = "SELECT id, client_id, client_name, client_id_issued_at, " +
                "client_authentication_methods, authorization_grant_types, redirect_uris, scopes, client_settings " +
                sql + "ORDER BY client_id_issued_at DESC LIMIT ? OFFSET ?";

        params.add(size);
        params.add(page * size);

        List<ClientSummaryResponse> content = jdbcTemplate.query(selectSql, (rs, rowNum) -> {
            String id = rs.getString("id");
            String clientId = rs.getString("client_id");
            String clientName = rs.getString("client_name");
            java.sql.Timestamp ts = rs.getTimestamp("client_id_issued_at");
            Instant issuedAt = ts != null ? ts.toInstant() : null;

            String authMethodsStr = rs.getString("client_authentication_methods");
            String grantTypesStr = rs.getString("authorization_grant_types");
            String redirectUrisStr = rs.getString("redirect_uris");
            String scopesStr = rs.getString("scopes");
            String clientSettingsStr = rs.getString("client_settings");

            Set<String> authMethods = parseCsv(authMethodsStr);
            Set<String> grantTypes = parseCsv(grantTypesStr);
            Set<String> scopes = parseCsv(scopesStr);
            int redirectUrisCount = parseCsv(redirectUrisStr).size();

            boolean requireProofKey = clientSettingsStr != null && clientSettingsStr.contains("\"require-proof-key\":true");
            boolean requireConsent = clientSettingsStr != null && clientSettingsStr.contains("\"require-authorization-consent\":true");

            return new ClientSummaryResponse(
                    id,
                    clientId,
                    clientName,
                    issuedAt,
                    authMethods,
                    grantTypes,
                    scopes,
                    redirectUrisCount,
                    requireProofKey,
                    requireConsent
            );
        }, params.toArray());

        return new PageResponse<>(
                content,
                new PageResponse.PageMetadata(page, size, totalElements, totalPages)
        );
    }

    @Transactional(readOnly = true)
    public ClientDetailsResponse getClientById(String id) {
        RegisteredClient client = registeredClientRepository.findById(id);
        if (client == null) {
            throw new NoSuchElementException("Client not found with ID: " + id);
        }
        return toDetailsResponse(client);
    }

    @Transactional(readOnly = true)
    public ClientDetailsResponse getClientByClientId(String clientId) {
        RegisteredClient client = registeredClientRepository.findByClientId(clientId);
        if (client == null) {
            throw new NoSuchElementException("Client not found with client_id: " + clientId);
        }
        return toDetailsResponse(client);
    }

    @Transactional
    public ClientDetailsResponse updateClient(String id, UpdateClientRequest request) {
        RegisteredClient existing = registeredClientRepository.findById(id);
        if (existing == null) {
            throw new NoSuchElementException("Client not found with ID: " + id);
        }

        long accessTtlMinutes = request.accessTokenTimeToLiveMinutes() != null && request.accessTokenTimeToLiveMinutes() > 0
                ? request.accessTokenTimeToLiveMinutes() : existing.getTokenSettings().getAccessTokenTimeToLive().toMinutes();
        long refreshTtlDays = request.refreshTokenTimeToLiveDays() != null && request.refreshTokenTimeToLiveDays() > 0
                ? request.refreshTokenTimeToLiveDays() : existing.getTokenSettings().getRefreshTokenTimeToLive().toDays();

        TokenSettings tokenSettings = TokenSettings.builder()
                .accessTokenFormat(OAuth2TokenFormat.SELF_CONTAINED)
                .accessTokenTimeToLive(Duration.ofMinutes(accessTtlMinutes))
                .refreshTokenTimeToLive(Duration.ofDays(refreshTtlDays))
                .authorizationCodeTimeToLive(Duration.ofMinutes(5))
                .reuseRefreshTokens(false)
                .build();

        ClientSettings clientSettings = ClientSettings.builder()
                .requireProofKey(Boolean.TRUE.equals(request.requireProofKey()))
                .requireAuthorizationConsent(Boolean.TRUE.equals(request.requireAuthorizationConsent()))
                .build();

        RegisteredClient.Builder builder = RegisteredClient.from(existing)
                .clientName(request.clientName().trim())
                .clientSettings(clientSettings)
                .tokenSettings(tokenSettings);

        builder.clientAuthenticationMethods(methods -> {
            methods.clear();
            for (String m : request.clientAuthenticationMethods()) {
                methods.add(resolveAuthenticationMethod(m));
            }
        });

        builder.authorizationGrantTypes(grants -> {
            grants.clear();
            for (String g : request.authorizationGrantTypes()) {
                grants.add(resolveGrantType(g));
            }
        });

        builder.redirectUris(uris -> {
            uris.clear();
            if (request.redirectUris() != null) {
                request.redirectUris().stream()
                        .filter(StringUtils::hasText)
                        .map(String::trim)
                        .forEach(uris::add);
            }
        });

        builder.postLogoutRedirectUris(uris -> {
            uris.clear();
            if (request.postLogoutRedirectUris() != null) {
                request.postLogoutRedirectUris().stream()
                        .filter(StringUtils::hasText)
                        .map(String::trim)
                        .forEach(uris::add);
            }
        });

        builder.scopes(scopes -> {
            scopes.clear();
            if (request.scopes() != null) {
                request.scopes().stream()
                        .filter(StringUtils::hasText)
                        .map(s -> s.trim().toLowerCase())
                        .forEach(scopes::add);
            }
        });

        RegisteredClient updated = builder.build();
        registeredClientRepository.save(updated);

        log.info("Updated OAuth2 client: {} (id: {})", updated.getClientId(), id);

        return toDetailsResponse(updated);
    }

    @Transactional
    public void deleteClient(String id) {
        RegisteredClient existing = registeredClientRepository.findById(id);
        if (existing == null) {
            throw new NoSuchElementException("Client not found with ID: " + id);
        }

        if (SYSTEM_REGISTRAR.equalsIgnoreCase(existing.getClientId())) {
            throw new IllegalStateException("System client '" + SYSTEM_REGISTRAR + "' cannot be deleted");
        }

        jdbcTemplate.update("DELETE FROM oauth2_authorization_consent WHERE registered_client_id = ?", id);
        jdbcTemplate.update("DELETE FROM oauth2_authorization WHERE registered_client_id = ?", id);
        jdbcTemplate.update("DELETE FROM oauth2_registered_client WHERE id = ?", id);

        log.info("Deleted OAuth2 client: {} (id: {})", existing.getClientId(), id);
    }

    @Transactional
    public RegenerateSecretResponse regenerateSecret(String id) {
        RegisteredClient existing = registeredClientRepository.findById(id);
        if (existing == null) {
            throw new NoSuchElementException("Client not found with ID: " + id);
        }

        String rawSecret = generateSecureSecret();
        String encodedSecret = passwordEncoder.encode(rawSecret);

        RegisteredClient updated = RegisteredClient.from(existing)
                .clientSecret(encodedSecret)
                .clientSecretExpiresAt(null)
                .build();

        registeredClientRepository.save(updated);

        log.info("Regenerated client secret for: {} (id: {})", existing.getClientId(), id);

        return new RegenerateSecretResponse(existing.getClientId(), rawSecret, Instant.now());
    }

    private ClientDetailsResponse toDetailsResponse(RegisteredClient c) {
        long accessTtlMinutes = c.getTokenSettings() != null && c.getTokenSettings().getAccessTokenTimeToLive() != null
                ? c.getTokenSettings().getAccessTokenTimeToLive().toMinutes() : 300;
        long refreshTtlDays = c.getTokenSettings() != null && c.getTokenSettings().getRefreshTokenTimeToLive() != null
                ? c.getTokenSettings().getRefreshTokenTimeToLive().toDays() : 7;

        boolean requireProofKey = c.getClientSettings() != null && c.getClientSettings().isRequireProofKey();
        boolean requireConsent = c.getClientSettings() != null && c.getClientSettings().isRequireAuthorizationConsent();

        return new ClientDetailsResponse(
                c.getId(),
                c.getClientId(),
                c.getClientName(),
                c.getClientIdIssuedAt(),
                c.getClientSecretExpiresAt(),
                c.getClientSecret() != null,
                c.getClientAuthenticationMethods().stream().map(ClientAuthenticationMethod::getValue).collect(Collectors.toSet()),
                c.getAuthorizationGrantTypes().stream().map(AuthorizationGrantType::getValue).collect(Collectors.toSet()),
                c.getRedirectUris(),
                c.getPostLogoutRedirectUris(),
                c.getScopes(),
                requireProofKey,
                requireConsent,
                accessTtlMinutes,
                refreshTtlDays
        );
    }

    private ClientAuthenticationMethod resolveAuthenticationMethod(String method) {
        String m = method != null ? method.trim().toLowerCase() : "";
        return switch (m) {
            case "client_secret_post" -> ClientAuthenticationMethod.CLIENT_SECRET_POST;
            case "none" -> ClientAuthenticationMethod.NONE;
            case "tls_client_auth" -> ClientAuthenticationMethod.TLS_CLIENT_AUTH;
            case "self_signed_tls_client_auth" -> ClientAuthenticationMethod.SELF_SIGNED_TLS_CLIENT_AUTH;
            default -> ClientAuthenticationMethod.CLIENT_SECRET_BASIC;
        };
    }

    private AuthorizationGrantType resolveGrantType(String grant) {
        String g = grant != null ? grant.trim().toLowerCase() : "";
        return switch (g) {
            case "refresh_token" -> AuthorizationGrantType.REFRESH_TOKEN;
            case "client_credentials" -> AuthorizationGrantType.CLIENT_CREDENTIALS;
            case "jwt-bearer" -> AuthorizationGrantType.JWT_BEARER;
            case "device_code" -> AuthorizationGrantType.DEVICE_CODE;
            default -> AuthorizationGrantType.AUTHORIZATION_CODE;
        };
    }

    private String generateSecureSecret() {
        byte[] bytes = new byte[32];
        RANDOM.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    private Set<String> parseCsv(String csv) {
        if (!StringUtils.hasText(csv)) return Collections.emptySet();
        return Arrays.stream(csv.split(","))
                .map(String::trim)
                .filter(StringUtils::hasText)
                .collect(Collectors.toSet());
    }
}
