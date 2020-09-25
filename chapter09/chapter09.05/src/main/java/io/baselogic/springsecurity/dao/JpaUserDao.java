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
 * @since chapter05.01 Created Class
 */
@Repository
@Validated
@Slf4j
public class JpaUserDao implements UserDao {

    private final AppUserRepository appUserRepository;
    private final RoleRepository roleRepository;

    @Autowired
    public JpaUserDao(final @NotNull AppUserRepository appUserRepository,
                      final @NotNull RoleRepository roleRepository) {
        this.appUserRepository = appUserRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public AppUser findById(final @NotNull Integer id) {
        Optional<AppUser> user = appUserRepository.findById(id);

        return user.orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public AppUser findByEmail(final @NotEmpty String email) {
        return appUserRepository.findByEmail(email);
    }

    // FIXME: Need to make a partial search:
    @Override
    @Transactional(readOnly = true)
    public List<AppUser> findAllByEmail(final @NotEmpty String partialEmail) {
        return appUserRepository.findAll();
    }


    @Override
    public Integer save(final @NotNull AppUser appUser) {

        Set<Role> roles = new HashSet<>();
        Optional<Role> role = roleRepository.findById(0);

        role.ifPresent(roles::add);

        appUser.setRoles(roles);

        AppUser result = appUserRepository.save(appUser);
        appUserRepository.flush();

        return result.getId();
    }

} // The End...
