package io.baselogic.springsecurity.authentication;

import io.baselogic.springsecurity.core.authority.EventUserAuthorityUtils;
import io.baselogic.springsecurity.core.userdetails.EventUserDetailsService;
import io.baselogic.springsecurity.domain.EventUser;
import io.baselogic.springsecurity.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collection;

/**
 * A Spring Security {@link AuthenticationProvider} that uses our {@link EventService} for authentication. Compare
 * this to our {@link EventUserDetailsService} which is called by Spring Security's {@link DaoAuthenticationProvider}.
 *
 * @author Rob Winch
 * @see EventUserDetailsService
 */
// Resolves a circular dependency:
//@Component
public class EventUserAuthenticationProvider implements AuthenticationProvider {

    private final EventService eventService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public EventUserAuthenticationProvider(final EventService eventService,
                                              final PasswordEncoder passwordEncoder) {
        if (eventService == null) {
            throw new IllegalArgumentException("eventService cannot be null");
        }
        if (passwordEncoder == null) {
            throw new IllegalArgumentException("passwordEncoder cannot be null");
        }

        this.eventService = eventService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) authentication;
        String email = token.getName();
        EventUser user = email == null ? null : eventService.findUserByEmail(email);
        if(user == null) {
            throw new UsernameNotFoundException("Invalid username/password");
        }
        // Database Password already encrypted:
        String password = user.getPassword();

        boolean passwordsMatch = passwordEncoder.matches(token.getCredentials().toString(), password);

        if(!passwordsMatch) {
            throw new BadCredentialsException("Invalid username/password");
        }
        Collection<? extends GrantedAuthority> authorities = EventUserAuthorityUtils.createAuthorities(user);
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(user, password, authorities);
        return usernamePasswordAuthenticationToken;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.equals(authentication);
    }
}
