package io.baselogic.springsecurity.repository;

import io.baselogic.springsecurity.domain.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AppUserRepository extends JpaRepository<AppUser, Integer> {

    AppUser findByEmail(String email);

    List<AppUser> findAllByEmailContaining(String partialEmail);

} // The End...
