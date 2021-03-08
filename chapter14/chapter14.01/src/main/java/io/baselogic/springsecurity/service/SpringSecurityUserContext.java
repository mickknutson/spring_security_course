package io.baselogic.springsecurity.service;

import io.baselogic.springsecurity.configuration.SecurityConfig;
import io.baselogic.springsecurity.core.authority.UserAuthorityUtils;
import io.baselogic.springsecurity.domain.AppUser;
import io.baselogic.springsecurity.domain.EventUserDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.configurers.provisioning.JdbcUserDetailsManagerConfigurer;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import reactor.core.publisher.Mono;
import reactor.util.context.Context;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.security.Principal;

/**
 * An implementation of {@link UserContext} that looks up the {@link AppUser} using the Spring Security's
 * {@link Authentication} by principal name.
 *
 * @author Mick Knutson
 * @since chapter03.01 Class Created
 * @since chapter03.02 Added {@link UserDetailsManager} support
 * @since chapter03.03 Changed {@link UserDetailsManager} to use custom @link EventUserDetailsService.
 * @since chapter03.04 simplify setCurrentUser(AppUser)
 * @since chapter04.01 Removed @Qualifier("eventUserDetailsService") to no longer require EventUserDetailsService.
 * @since chapter04.02 added conversion to/from {@link org.springframework.security.core.userdetails.User}
 * @since chapter14.01 Updated for Reactive WebFlux
 */
@Component
@Slf4j
public class SpringSecurityUserContext implements UserContext {

    @Autowired
    private ReactiveAuthenticationManager authenticationManager;

    private final EventService eventService;
    private final ReactiveUserDetailsService userDetailsService;

    public SpringSecurityUserContext(final EventService eventService,
                                     final ReactiveUserDetailsService userDetailsService) {
        Assert.notNull(eventService, "eventService cannot be null");
        Assert.notNull(userDetailsService, "userDetailsService cannot be null");

        this.eventService = eventService;
        this.userDetailsService = userDetailsService;
    }


    /**
     * Get the {@link AppUser} by obtaining the currently logged in Spring Security user's
     * {@link Authentication#getName()} and using that to find the {@link AppUser} by email address (since for our
     * application Spring Security username's are email addresses).
     *
     * @since chapter14.01 We create {@link JdbcUserDetailsManagerConfigurer}
     * in {@link SecurityConfig#configure(ServerHttpSecurity)}.
     * Additionally I added {@link UserAuthorityUtils#getUserEmail(Object)} call as
     * with the default {@link JdbcUserDetailsManagerConfigurer}, we get a {@link User}
     * not a {@link EventUserDetails}.
     */
    @Override
    public Mono<AppUser> getCurrentUser() {

        return ReactiveSecurityContextHolder.getContext()
            .map(SecurityContext::getAuthentication)
                .map(authentication -> {
                    log.info("*** Reactive authentication [{}]", authentication);
                    return UserAuthorityUtils.getUserEmail(
                            authentication.getPrincipal()
                    );
                }).flatMap(eventService::findUserByEmail)
                .doOnSuccess(a -> {
                    log.info("* findUserByEmail({})", a);

                }).log("get_CURRENT_USER");
    }

    /**
     * Sets the {@link AppUser} as the current {@link Authentication}'s
     * principal.
     */
    @Override
    public Mono<Void> setCurrentUser(final @Valid @NotNull(message = "user.notNull.key") AppUser appUser) {
        log.debug("* setCurrentUser({})", appUser);

        if (appUser.getEmail() == null) {
            throw new IllegalArgumentException("email cannot be null");
        }

        Mono<UserDetails> userDetailsMono = userDetailsService.findByUsername(appUser.getEmail())
                .map(userDetails -> {
                    log.info("* userDetails ({})", userDetails);
                    return new UsernamePasswordAuthenticationToken(
                            userDetails,
                            appUser.getPassword(),
                            userDetails.getAuthorities());
                })
                .flatMap(authenticationManager::authenticate)
                .map(authentication -> {
                    log.info("* auth ({})", authentication);
                    SecurityContext ctx = new SecurityContextImpl(authentication);
                    Context context = ReactiveSecurityContextHolder.withSecurityContext(
                            Mono.just(ctx));

                    log.info("* ctx.auth ({})", ctx.getAuthentication());
                    log.info("* context ({})", context);
                    context.stream().forEach(e -> log.info("* e ({})", e));

                    return (UserDetails) authentication.getPrincipal();

                });

        return userDetailsMono.doOnSuccess(u -> log.info("* u ({})", u)).then();

    }

} // The End...
