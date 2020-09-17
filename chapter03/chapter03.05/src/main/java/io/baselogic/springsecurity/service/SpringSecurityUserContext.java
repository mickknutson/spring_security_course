package io.baselogic.springsecurity.service;

import io.baselogic.springsecurity.core.authority.UserAuthorityUtils;
import io.baselogic.springsecurity.domain.AppUser;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Collection;

/**
 * An implementation of {@link UserContext} that looks up the {@link AppUser} using the Spring Security's
 * {@link Authentication} by principal name.
 *
 * @author Mick Knutson
 * @since chapter03.01 Class Created
 * @since chapter03.02 Added {@link UserDetailsManager} support
 * @since chapter03.03 Changed {@link UserDetailsManager} to use custom EventUserDetailsService.
 * @since chapter03.04 simplify setCurrentUser(AppUser)
 */
@Component
public class SpringSecurityUserContext implements UserContext {

    /**
     * Get the {@link AppUser} by obtaining the currently logged in Spring Security user's
     * {@link Authentication#getName()} and using that to find the {@link AppUser} by email address (since for our
     * application Spring Security username's are email addresses).
     *
     * @since chapter03.04:
     * Get the {@link AppUser} by casting
     * the {@link Authentication}'s principal to a {@link AppUser}.
     */
    @Override
    public AppUser getCurrentUser() {
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        if (authentication == null) {
            return null;
        }
        return (AppUser) authentication.getPrincipal();
    }

    /**
     * Sets the {@link AppUser} as the current {@link Authentication}'s
     * principal.
     */
    @Override
    public void setCurrentUser(final @Valid @NotNull(message = "user.notNull.key") AppUser appUser) {
        if (appUser.getEmail() == null) {
            throw new IllegalArgumentException("email cannot be null");
        }
        Collection<GrantedAuthority> authorities = UserAuthorityUtils.createAuthorities(appUser);
        Authentication authentication = new UsernamePasswordAuthenticationToken(appUser,
                appUser.getPassword(), authorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

} // The End...
