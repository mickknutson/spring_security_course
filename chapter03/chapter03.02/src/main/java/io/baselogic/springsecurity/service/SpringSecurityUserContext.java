package io.baselogic.springsecurity.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * An implementation of {@link UserContext} that looks up the {@link io.baselogic.springsecurity.domain.User} using the Spring Security's
 * {@link Authentication} by principal name.
 *
 * @since chapter03.01
 * @author Mick Knutson
 *
 */
@Component
public class SpringSecurityUserContext implements UserContext {

    private final EventService eventService;
    private final UserDetailsService userDetailsService;

    @Autowired
    public SpringSecurityUserContext(final @NotNull EventService eventService,
                                     final @NotNull UserDetailsService userDetailsService) {
        this.eventService = eventService;
        this.userDetailsService = userDetailsService;
    }

    /**
     * Get the {@link io.baselogic.springsecurity.domain.User} by obtaining the currently logged in Spring Security user's
     * {@link Authentication#getName()} and using that to find the {@link io.baselogic.springsecurity.domain.User} by email address (since for our
     * application Spring Security username's are email addresses).
     */
    @Override
    public io.baselogic.springsecurity.domain.User getCurrentUser() {

        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();

        if (authentication == null) {
            return null;
        }

        User user = (User)authentication.getPrincipal();
        String email = user.getUsername();

        if (email == null) {
            return null;
        }

        io.baselogic.springsecurity.domain.User result = eventService.findUserByEmail(email);
        if (result == null) {
            throw new IllegalStateException(
                    "Spring Security is not in synch with Users. Could not find user with email " + email);
        }
        return result;
    }

    @Override
    public void setCurrentUser(final @Valid @NotNull(message="user.notNull.key") io.baselogic.springsecurity.domain.User user) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails,
                user.getPassword(),userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

} // The End...
