package io.baselogic.springsecurity.service;

import io.baselogic.springsecurity.domain.AppUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * An implementation of {@link UserContext} that looks up the {@link AppUser} using the Spring Security's
 * {@link Authentication} by principal name.
 *
 * @author Mick Knutson
 * @since chapter03.01 Class Created
 */
@Component
public class SpringSecurityUserContext implements UserContext {

    private final EventService eventService;

    @Autowired
    public SpringSecurityUserContext(final @NotNull EventService eventService) {
        this.eventService = eventService;
    }

    /**
     * Get the {@link AppUser} by obtaining the currently logged in Spring Security user's
     * {@link Authentication#getName()} and using that to find the {@link AppUser} by email address (since for our
     * application Spring Security username's are email addresses).
     *
     */
    @Override
    public AppUser getCurrentUser() {

        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();

        if (authentication == null) {
            return null;
        }

        User user = (User)authentication.getPrincipal();
        String email = user.getUsername();

        AppUser result = eventService.findUserByEmail(email);

        if (result == null) {
            throw new IllegalStateException(
                    "Spring Security is not in synch with Users. Could not find user with email " + email);
        }
        return result;
    }

    @Override
    public void setCurrentUser(final @Valid @NotNull(message="user.notNull.key") AppUser appUser) {
        throw new UnsupportedOperationException();
    }

} // The End...
