package io.baselogic.springsecurity.authentication;

import io.baselogic.springsecurity.core.authority.UserAuthorityUtils;
import io.baselogic.springsecurity.domain.User;
import io.baselogic.springsecurity.service.EventService;
import io.baselogic.springsecurity.userdetails.EventUserDetailsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.util.Collection;

/**
 * A Spring Security {@link AuthenticationProvider} that uses our {@link EventService} for authentication.
 * Compare this to our {@link EventUserDetailsService} which is called by
 * Spring Security's {@link DaoAuthenticationProvider}.
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
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        DomainUsernamePasswordAuthenticationToken token = (DomainUsernamePasswordAuthenticationToken) authentication;
        String userName = token.getName();
        String domain = token.getDomain();
        String email = userName + "@" + domain;

//        User user = email == null ? null : calendarService.findUserByEmail(email);
        User user = eventService.findUserByEmail(email);
        log.info("User: {}", user);

        if(user == null) {
            throw new UsernameNotFoundException("Invalid username/password");
        }

        // FIXME: Need to configure password encoder support to account for {noop} prefix in passwords
        String password = user.getPassword();
        log.info("Password: {}", password);
        log.info("Credentials: {}", token.getCredentials());

        if(!password.equals(token.getCredentials())) {
            throw new BadCredentialsException("Invalid username/password");
        }
        Collection<? extends GrantedAuthority> authorities = UserAuthorityUtils.createAuthorities(user);
        log.info("authorities: {}", authorities);
        return new DomainUsernamePasswordAuthenticationToken(user, password, domain, authorities);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return DomainUsernamePasswordAuthenticationToken.class.equals(authentication);
    }

} // The End...
