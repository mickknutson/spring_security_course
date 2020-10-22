package io.baselogic.springsecurity.repository;

import io.baselogic.springsecurity.domain.AppUser;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface AppUserRepository extends MongoRepository<AppUser, Integer> {

    AppUser findByEmail(String email);

    List<AppUser> findAllByEmailContaining(String partialEmail);

} // The End...
