package kh.edu.istad.iam.restapi.service;

import kh.edu.istad.iam.restapi.dto.response.AuthResponse;
import org.springframework.security.core.Authentication;

import java.util.UUID;

public interface TokenService {
    AuthResponse issueTokens(Authentication authentication);
    AuthResponse issueTokens(Authentication authentication, UUID sessionId);
}