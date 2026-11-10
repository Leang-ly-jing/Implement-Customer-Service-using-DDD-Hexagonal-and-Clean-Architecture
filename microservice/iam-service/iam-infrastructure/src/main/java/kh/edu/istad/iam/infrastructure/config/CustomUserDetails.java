package kh.edu.istad.iam.infrastructure.config;


import kh.edu.istad.iam.domain.dto.result.AuthUserResult;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class CustomUserDetails implements UserDetails {

    private final AuthUserResult authUser;

    public CustomUserDetails(AuthUserResult authUser) {
        this.authUser = authUser;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authUser.authorities().stream()
                .map(SimpleGrantedAuthority::new)
                .toList();
    }

    public AuthUserResult getAuthUser() {
        return authUser;
    }

    @Override
    public String getPassword() {
        return authUser.passwordHash();
    }

    @Override
    public String getUsername() {
        return authUser.username();
    }

    @Override
    public boolean isAccountNonExpired() {
        return authUser.accountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return authUser.accountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return authUser.credentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return authUser.enabled();
    }
}