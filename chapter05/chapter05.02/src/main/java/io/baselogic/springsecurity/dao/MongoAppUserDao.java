package io.baselogic.springsecurity.dao;

import io.baselogic.springsecurity.domain.AppUser;
import io.baselogic.springsecurity.domain.Role;
import io.baselogic.springsecurity.repository.AppUserRepository;
import io.baselogic.springsecurity.repository.RoleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Repository;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * A MongoDb Document implementation of {@link UserDao}.
 *
 * @author Mick Knutson
 * @since chapter05.02 Created Class
 */
@Repository
@Validated
@Slf4j
public class MongoAppUserDao implements UserDao {

    private final AppUserRepository appUserRepository;
    private final RoleRepository roleRepository;

    // Simple Primary Key Generator
    private final AtomicInteger userPK = new AtomicInteger(10);


    @Autowired
    public MongoAppUserDao(final @NotNull AppUserRepository appUserRepository,
                           final @NotNull RoleRepository roleRepository) {
        this.appUserRepository = appUserRepository;
        this.roleRepository = roleRepository;
    }


    @Override
    public AppUser findById(final @NotNull Integer id) {
        return appUserRepository.findById(id).orElse(null);
    }

    @Override
    public AppUser findByEmail(final @NotEmpty String email) {
        return appUserRepository.findByEmail(email);
    }

    @Override
    public List<AppUser> findAllByEmail(@NotEmpty String partialEmail) {
        return appUserRepository.findAll();
    }

    @Override
    public Integer save(final @NotNull AppUser appUser) {

        Role example = new Role();
        example.setId(0);

        Set<Role> roles = new HashSet<>();

        roles.add(
                roleRepository.findOne(Example.of(example))
                .orElseThrow(()-> new EmptyResultDataAccessException(1))
        );

        appUser.setRoles(roles);

        if(appUser.getId() == null) {
            // Get the next PK instance
            appUser.setId(userPK.incrementAndGet());
        }


        AppUser result = appUserRepository.save(appUser);

        return result.getId();
    }

} // The End...
