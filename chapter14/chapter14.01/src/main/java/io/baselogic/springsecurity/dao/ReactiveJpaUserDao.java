package io.baselogic.springsecurity.dao;

import io.baselogic.springsecurity.domain.AppUser;
import io.baselogic.springsecurity.domain.Event;
import io.baselogic.springsecurity.domain.Role;
import io.baselogic.springsecurity.repository.AppUserRepository;
import io.baselogic.springsecurity.repository.RoleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

/**
 * A ReactiveMongoJPA implementation of {@link UserDao}.
 *
 * @author Mick Knutson
 *
 * @since chapter14.01 Refactored class for WebFlux
 */
@Repository
@Slf4j
public class ReactiveJpaUserDao implements UserDao {

    private final AppUserRepository appUserRepository;
    private final RoleRepository roleRepository;
    private final AppUserNumberGenerator appUserNumberGenerator;

    public ReactiveJpaUserDao(final AppUserRepository appUserRepository,
                              final RoleRepository roleRepository,
                              final AppUserNumberGenerator appUserNumberGenerator) {
        Assert.notNull(appUserRepository, "appUserRepository cannot be null");
        Assert.notNull(roleRepository, "roleRepository cannot be null");
        Assert.notNull(appUserNumberGenerator, "appUserNumberGenerator cannot be null");

        this.appUserRepository = appUserRepository;
        this.roleRepository = roleRepository;
        this.appUserNumberGenerator = appUserNumberGenerator;
    }

    @Override
    public Mono<AppUser> findById(final @NotNull Integer id) {
        return appUserRepository.findById(id);
    }

    @Override
    public Mono<AppUser> findByEmail(final @NotEmpty String email) {
        log.debug("findByEmail: {}", email);
        return appUserRepository.findByEmail(email);
    }

    @Override
    public Flux<AppUser> findAllByEmail(final @NotEmpty String partialEmail) {
        return appUserRepository.findAllByEmailContaining(partialEmail);
    }


    @Override
    public Mono<AppUser> save(final @NotNull AppUser appUser) {

        appUser.setId(appUserNumberGenerator.getNextGivenNumber());

        Set<Role> roles = new HashSet<>();

        // TODO: Refactor to zithWhen
        Mono<Role> roleMono = roleRepository.findById(0)
                .doOnSuccess(r -> {
                    log.trace("find ROLE: {}", r);
                });

        Mono<AppUser> updatedUser = roleMono.map(role -> {
            roles.add(role);
            appUser.setRoles(roles);
            log.trace("updated appUser: {}", appUser);
            return appUser;
        });

        return updatedUser.flatMap(appUserRepository::save)
                .doOnSuccess(u -> log.trace("*** saved appUser: {} ***", u));

    }

} // The End...
