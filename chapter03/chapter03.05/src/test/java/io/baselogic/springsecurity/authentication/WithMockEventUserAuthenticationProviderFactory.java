package io.baselogic.springsecurity.authentication;

import io.baselogic.springsecurity.annotations.WithMockEventUserDetails;
import io.baselogic.springsecurity.annotations.WithMockEventUserDetailsUser1;
import io.baselogic.springsecurity.core.authority.UserAuthorityUtils;
import io.baselogic.springsecurity.domain.AppUser;
import io.baselogic.springsecurity.domain.EventUserDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.util.Collection;

/**
 * Mock SecurityContextFactory for {@link WithMockEventUserDetailsUser1} annotation
 */
@Slf4j
public class WithMockEventUserAuthenticationProviderFactory
        implements WithSecurityContextFactory<WithMockEventUserDetails> {

    // Placeholder: Allow Autowiring AuthenticationProvider

    @Override
    public SecurityContext createSecurityContext(WithMockEventUserDetails mockUser){
        SecurityContext context =
                SecurityContextHolder.createEmptyContext();

        AppUser user = new AppUser();
        user.setId(mockUser.id());
        user.setFirstName(mockUser.name());
        user.setPassword(mockUser.password());
        user.setEmail(mockUser.username());

        EventUserDetails principal = new EventUserDetails(user);

        Collection<GrantedAuthority> authorities = UserAuthorityUtils.createAuthorities(principal);

        Authentication authentication =
                new UsernamePasswordAuthenticationToken(principal, "password",
                        authorities);
        context.setAuthentication(authentication);

        log.info("*** Setting MockEventUserDetails to SecurityContext: {}", authentication);
        log.info("*** principal: {}", principal);
        return context;
    }
} // The End...
