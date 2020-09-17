package io.baselogic.springsecurity.authentication;

import io.baselogic.springsecurity.core.authority.UserAuthorityUtils;
import io.baselogic.springsecurity.domain.AppUser;
import io.baselogic.springsecurity.domain.EventUserDetails;
import io.baselogic.springsecurity.service.EventService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.validation.constraints.NotNull;
import java.util.Collection;

/**
 * A Spring Security {@link AuthenticationProvider} that uses our {@link EventService} for authentication.
 *
 * Compare this to our EventUserDetailsService which is called by
 * Spring Security's {@link DaoAuthenticationProvider}.
 *
 * @author mickknutson
 *
 * @since chapter03.05 Created Class
 * @since chapter03.06 Added DomainUsernamePasswordAuthenticationToken support
 */
@Slf4j
public class EventUserAuthenticationProvider implements AuthenticationProvider {

    private final EventService eventService;
    private final PasswordEncoder passwordEncoder;

    public EventUserAuthenticationProvider(final @NotNull PasswordEncoder passwordEncoder,
                                           final @NotNull EventService eventService) {
        this.eventService = eventService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        DomainUsernamePasswordAuthenticationToken token = (DomainUsernamePasswordAuthenticationToken) authentication;

        String userName = token.getName();
        String domain = token.getDomain();
        String email = userName + "@" + domain;

        log.info("*** Attempting EventUserAuthenticationProvider.authenticate('{}')", email);

        AppUser appUser = eventService.findUserByEmail(email);

        if(appUser == null) {
            throw new UsernameNotFoundException("Invalid username/password/domain");
        }

        String encodedPassword = appUser.getPassword();
        String presentedPassword = authentication.getCredentials().toString();

        log.info("Encoded Password: {}", encodedPassword);
        log.info("Authentication Credentials: {}", presentedPassword);

        if ( ! passwordEncoder.matches(presentedPassword, encodedPassword)) {
            log.debug("Authentication failed: password does not match stored value");
            throw new BadCredentialsException("Invalid username/password/domain");
        }

        Collection<GrantedAuthority> authorities = UserAuthorityUtils.createAuthorities(appUser);
        log.info("return valid DomainUsernamePasswordAuthenticationToken");

        return new DomainUsernamePasswordAuthenticationToken(
                new EventUserDetails(appUser),
                presentedPassword,
                domain,
                authorities);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return DomainUsernamePasswordAuthenticationToken.class.equals(authentication);
    }

} // The End...
