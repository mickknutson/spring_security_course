package io.baselogic.springsecurity.dao;

import io.baselogic.springsecurity.domain.AppUser;
import io.baselogic.springsecurity.domain.Role;
import io.baselogic.springsecurity.repository.AppUserRepository;
import io.baselogic.springsecurity.repository.RoleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * A JPA implementation of {@link UserDao}.
 *
 * @author Mick Knutson
 *
 * @since chapter14.01 Refactored class for WebFlux
 */
@Repository
@Validated
@Slf4j
public class ReactiveJpaUserDao implements UserDao {

    private final AppUserRepository appUserRepository;
    private final RoleRepository roleRepository;

    @Autowired
    public ReactiveJpaUserDao(final @NotNull AppUserRepository appUserRepository,
                              final @NotNull RoleRepository roleRepository) {
        this.appUserRepository = appUserRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public Mono<AppUser> findById(final @NotNull Integer id) {
        return appUserRepository.findById(id);
    }

    @Override
    public Mono<AppUser> findByEmail(final @NotEmpty String email) {
        return appUserRepository.findByEmail(email);
    }

    @Override
    public Flux<AppUser> findAllByEmail(final @NotEmpty String partialEmail) {
        return appUserRepository.findAllByEmailContaining(partialEmail);
    }


    @Override
    public Mono<Integer> save(final @NotNull AppUser appUser) {

        Set<Role> roles = new HashSet<>();
        Mono<Role> role = roleRepository.findById(0);

        roles.add(role.block());
        appUser.setRoles(roles);

        Mono<AppUser> result = appUserRepository.save(appUser);

        return result.map( r -> r.getId());
    }

} // The End...
