package io.baselogic.springsecurity.authentication;

import io.baselogic.springsecurity.core.authority.UserAuthorityUtils;
import io.baselogic.springsecurity.domain.User;
import io.baselogic.springsecurity.service.EventService;
import io.baselogic.springsecurity.userdetails.EventUserDetailsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.util.Collection;

/**
 * A Spring Security {@link AuthenticationProvider} that uses our {@link EventService} for authentication. Compare
 * this to our {@link EventUserDetailsService} which is called by Spring Security's {@link DaoAuthenticationProvider}.
 *
 * @author mickknutson
 * @see EventUserDetailsService
 *
 * @since chapter03.05
 */
@Component
@Slf4j
public class EventUserAuthenticationProvider implements AuthenticationProvider {

    private final EventService eventService;

    @Autowired
    public EventUserAuthenticationProvider(final @NotNull EventService eventService) {
        this.eventService = eventService;
    }

    @Override
    public Authentication authenticate(final Authentication authentication) throws AuthenticationException {
        UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) authentication;

        String email = token.getName();
        log.info("authenticate: {}", email);

        User user = email == null ? null : eventService.findUserByEmail(email);

        if(user == null) {
            throw new UsernameNotFoundException("Invalid username/password");
        }

        String password = user.getPassword();
        log.info("Password: {}", password);
        log.info("Credentials: {}", token.getCredentials());

        if(!password.equals(token.getCredentials())) {
            log.info("BadCredentialsException(\"Invalid username/password\")");
            throw new BadCredentialsException("Invalid username/password");
        }

        Collection<GrantedAuthority> authorities = UserAuthorityUtils.createAuthorities(user);
        log.info("return valid UsernamePasswordAuthenticationToken");

        return new UsernamePasswordAuthenticationToken(user, password, authorities);
    }

    @Override
    public boolean supports(final Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.equals(authentication);
    }

} // The End...
