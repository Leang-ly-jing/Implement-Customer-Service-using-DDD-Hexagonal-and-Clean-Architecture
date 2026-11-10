package kh.edu.istad.iam.infrastructure.config;

import kh.edu.istad.iam.domain.dto.result.AuthUserResult;
import kh.edu.istad.iam.domain.usecase.user.LoadUserForAuthenticationUseCase;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserDetailServiceImpl implements UserDetailsService {

    private final LoadUserForAuthenticationUseCase loadUserForAuthenticationUseCase;

    public UserDetailServiceImpl(LoadUserForAuthenticationUseCase loadUserForAuthenticationUseCase) {
        this.loadUserForAuthenticationUseCase = loadUserForAuthenticationUseCase;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            AuthUserResult authUser = loadUserForAuthenticationUseCase.execute(username);
            List<SimpleGrantedAuthority> authorities = authUser.authorities() != null
                    ? authUser.authorities().stream().map(SimpleGrantedAuthority::new).toList()
                    : List.of(new SimpleGrantedAuthority("ROLE_USER"));

            return new User(
                    authUser.username(),
                    authUser.passwordHash() != null ? authUser.passwordHash() : "",
                    authUser.enabled(),
                    authUser.accountNonExpired(),
                    authUser.credentialsNonExpired(),
                    authUser.accountNonLocked(),
                    authorities
            );
        } catch (Exception e) {
            throw new UsernameNotFoundException("User not found: " + username, e);
        }
    }
}
