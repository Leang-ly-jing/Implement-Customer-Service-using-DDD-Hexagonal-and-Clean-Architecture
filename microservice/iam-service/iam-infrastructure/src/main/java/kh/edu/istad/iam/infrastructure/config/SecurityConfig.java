package kh.edu.istad.iam.infrastructure.config;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.authorization.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.oidc.authentication.OidcIdTokenDecoderFactory;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtDecoderFactory;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.server.authorization.JdbcOAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.JdbcOAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2ClientAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.client.JdbcRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.util.matcher.MediaTypeRequestMatcher;
import org.springframework.util.StringUtils;

import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.time.Instant;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JdbcTemplate jdbcTemplate;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final CustomOidcUserService customOidcUserService;

    @Bean
    @Order(1)
    public SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http) throws Exception {
        OAuth2AuthorizationServerConfigurer authorizationServerConfigurer = new OAuth2AuthorizationServerConfigurer();

        http
                .securityMatcher(authorizationServerConfigurer.getEndpointsMatcher())
                .with(authorizationServerConfigurer, (authorizationServer) ->
                        authorizationServer
                                .authorizationEndpoint(authorizationEndpoint ->
                                        authorizationEndpoint.consentPage("/oauth2/consent")
                                )
                                .clientAuthentication(clientAuthentication ->
                                        clientAuthentication.authenticationConverters(converters ->
                                                converters.add(0, new TolerantClientBasicAuthenticationConverter())
                                        )
                                )
                                .oidc((oidc) -> oidc
                                        .clientRegistrationEndpoint(Customizer.withDefaults())
                                )
                )
                .authorizeHttpRequests((authorize) ->
                        authorize
                                .anyRequest().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()))
                .exceptionHandling((exceptions) -> exceptions
                        .defaultAuthenticationEntryPointFor(
                                new LoginUrlAuthenticationEntryPoint("/login"),
                                new MediaTypeRequestMatcher(MediaType.TEXT_HTML)
                        )
                );

        return http.build();
    }

    @Bean
    @Order(2)
    public SecurityFilterChain apiSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/api/**")
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(HttpMethod.POST, "/api/auth/register").permitAll()
                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter())));

        return http.build();
    }

    @Bean
    @Order(3)
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers("/login", "/register", "/login/telegram", "/css/**", "/js/**", "/images/**", "/favicon.ico", "/error").permitAll()
                        .anyRequest().authenticated()
                )
                .oauth2Login(config -> config
                        .loginPage("/login")
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(customOAuth2UserService)
                                .oidcUserService(customOidcUserService)
                        )
                        .permitAll()
                )
                .oauth2Client(Customizer.withDefaults())
                .formLogin(config -> config
                        .loginPage("/login")
                        .permitAll()
                );

        return http.build();
    }

    @Bean
    public JwtDecoderFactory<ClientRegistration> idTokenDecoderFactory() {
        OidcIdTokenDecoderFactory defaultFactory = new OidcIdTokenDecoderFactory();
        return clientRegistration -> {
            if ("telegram".equalsIgnoreCase(clientRegistration.getRegistrationId())) {
                return token -> {
                    try {
                        SignedJWT signedJWT = SignedJWT.parse(token);
                        JWTClaimsSet claimsSet = signedJWT.getJWTClaimsSet();
                        Instant issuedAt = claimsSet.getIssueTime() != null ? claimsSet.getIssueTime().toInstant() : Instant.now();
                        Instant expiresAt = claimsSet.getExpirationTime() != null ? claimsSet.getExpirationTime().toInstant() : issuedAt.plusSeconds(3600);
                        return new Jwt(token, issuedAt, expiresAt, signedJWT.getHeader().toJSONObject(), claimsSet.getClaims());
                    } catch (Exception e) {
                        throw new JwtException("Failed to decode Telegram ID token: " + e.getMessage(), e);
                    }
                };
            }
            return defaultFactory.createDecoder(clientRegistration);
        };
    }

    @Bean
    public RegisteredClientRepository registeredClientRepository() {
        JdbcRegisteredClientRepository repository = new JdbcRegisteredClientRepository(jdbcTemplate);

        if (repository.findByClientId("registrar-client") == null) {
            RegisteredClient registrarClient = RegisteredClient.withId(UUID.randomUUID().toString())
                    .clientId("registrar-client")
                    .clientSecret("{noop}secret")
                    .clientName("Registrar Client")
                    .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                    .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
                    .scope("client.create")
                    .scope("client.read")
                    .build();
            repository.save(registrarClient);
        }

        RegisteredClient existingOidc = repository.findByClientId("oidc-client");
        if (existingOidc != null) {
            jdbcTemplate.update("DELETE FROM oauth2_registered_client WHERE client_id = ?", "oidc-client");
        }

        org.springframework.security.oauth2.server.authorization.settings.TokenSettings tokenSettings =
                org.springframework.security.oauth2.server.authorization.settings.TokenSettings.builder()
                        .accessTokenFormat(org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat.SELF_CONTAINED)
                        .accessTokenTimeToLive(java.time.Duration.ofHours(5))
                        .refreshTokenTimeToLive(java.time.Duration.ofDays(7))
                        .authorizationCodeTimeToLive(java.time.Duration.ofMinutes(3))
                        .reuseRefreshTokens(false)
                        .build();

        ClientSettings clientSettings = ClientSettings.builder()
                .requireProofKey(false)
                .requireAuthorizationConsent(true)
                .build();

        RegisteredClient oidcClient = RegisteredClient.withId(UUID.randomUUID().toString())
                .clientId("oidc-client")
                .clientSecret("{noop}oidc-secret")
                .clientName("OIDC Client")
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_POST)
                .clientAuthenticationMethod(ClientAuthenticationMethod.NONE)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
                .redirectUri("http://127.0.0.1:8080/login/oauth2/code/oidc-client")
                .postLogoutRedirectUri("http://127.0.0.1:8080/")
                .scope(OidcScopes.OPENID)
                .scope(OidcScopes.PROFILE)
                .clientSettings(clientSettings)
                .tokenSettings(tokenSettings)
                .build();
        repository.save(oidcClient);
        return repository;
    }

    @Bean
    public OAuth2AuthorizationService oAuth2AuthorizationService() {
        try {
            jdbcTemplate.execute("DELETE FROM oauth2_authorization WHERE principal_name IS NOT NULL");
        } catch (Exception ignored) {
        }
        return new JdbcOAuth2AuthorizationService(jdbcTemplate, registeredClientRepository());
    }

    @Bean
    public OAuth2AuthorizationConsentService oAuth2AuthorizationConsentService() {
        return new JdbcOAuth2AuthorizationConsentService(jdbcTemplate, registeredClientRepository());
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        grantedAuthoritiesConverter.setAuthorityPrefix("");
        grantedAuthoritiesConverter.setAuthoritiesClaimName("authorities");

        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter);
        return jwtAuthenticationConverter;
    }

    @Bean
    public JWKSource<SecurityContext> jwkSource() {
        KeyPair keyPair = generateRsaKey();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        RSAKey rsaKey = new RSAKey.Builder(publicKey)
                .privateKey(privateKey)
                .keyID(UUID.randomUUID().toString())
                .build();
        JWKSet jwkSet = new JWKSet(rsaKey);
        return new ImmutableJWKSet<>(jwkSet);
    }

    private static KeyPair generateRsaKey() {
        KeyPair keyPair;
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            keyPair = keyPairGenerator.generateKeyPair();
        } catch (Exception ex) {
            throw new IllegalStateException(ex);
        }
        return keyPair;
    }

    @Bean
    public JwtDecoder jwtDecoder(JWKSource<SecurityContext> jwkSource) {
        return OAuth2AuthorizationServerConfiguration.jwtDecoder(jwkSource);
    }

    @Bean
    public AuthorizationServerSettings authorizationServerSettings() {
        return AuthorizationServerSettings.builder()
                .issuer("http://localhost:9000")
                .build();
    }

    private static class TolerantClientBasicAuthenticationConverter implements AuthenticationConverter {
        @Override
        public Authentication convert(HttpServletRequest request) {
            String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
            if (!StringUtils.startsWithIgnoreCase(authorization, "basic ")) {
                return null;
            }
            try {
                byte[] decoded = Base64.getDecoder().decode(authorization.substring(6).trim());
                String credentials = new String(decoded, StandardCharsets.UTF_8);
                int delim = credentials.indexOf(":");
                String clientId = delim == -1 ? credentials.trim() : credentials.substring(0, delim).trim();
                String clientSecret = delim == -1 ? "" : credentials.substring(delim + 1).trim();
                if (!StringUtils.hasText(clientSecret)) {
                    Map<String, Object> additionalParameters = new HashMap<>();
                    request.getParameterMap().forEach((k, v) -> {
                        if (!"client_id".equals(k) && !"client_secret".equals(k)) {
                            additionalParameters.put(k, v != null && v.length == 1 ? v[0] : v);
                        }
                    });
                    return new OAuth2ClientAuthenticationToken(clientId, ClientAuthenticationMethod.NONE, null, additionalParameters);
                }
            } catch (Exception ignored) {
            }
            return null;
        }
    }
}
