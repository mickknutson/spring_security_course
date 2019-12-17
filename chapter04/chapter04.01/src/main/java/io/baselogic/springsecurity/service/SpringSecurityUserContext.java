package io.baselogic.springsecurity.service;

import io.baselogic.springsecurity.core.authority.UserAuthorityUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Collection;

/**
 * An implementation of {@link UserContext} that looks up the {@link io.baselogic.springsecurity.domain.User} using the Spring Security's
 * {@link Authentication} by principal name.
 *
 * @author Mick Knutson
 * @since chapter03.01
 */
@Component
public class SpringSecurityUserContext implements UserContext {

    /**
     * Get the {@link io.baselogic.springsecurity.domain.User} by obtaining the currently logged in Spring Security user's
     * {@link Authentication#getName()} and using that to find the {@link io.baselogic.springsecurity.domain.User} by email address (since for our
     * application Spring Security username's are email addresses).
     *
     * @since chapter03.04:
     * Get the {@link io.baselogic.springsecurity.domain.User} by casting
     * the {@link Authentication}'s principal to a {@link io.baselogic.springsecurity.domain.User}.
     */
    @Override
    public io.baselogic.springsecurity.domain.User getCurrentUser() {
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        if (authentication == null) {
            return null;
        }
        return (io.baselogic.springsecurity.domain.User) authentication.getPrincipal();
    }

    /**
     * Sets the {@link io.baselogic.springsecurity.domain.User} as the current {@link Authentication}'s
     * principal.
     */
    @Override
    public void setCurrentUser(final @Valid @NotNull(message = "user.notNull.key") io.baselogic.springsecurity.domain.User user) {
        if (user.getEmail() == null) {
            throw new IllegalArgumentException("email cannot be null");
        }
        Collection<GrantedAuthority> authorities = UserAuthorityUtils.createAuthorities(user);
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user,
                user.getPassword(), authorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

} // The End...
