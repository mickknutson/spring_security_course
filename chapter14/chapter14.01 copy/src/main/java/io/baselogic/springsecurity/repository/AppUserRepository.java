package io.baselogic.springsecurity.repository;

import io.baselogic.springsecurity.domain.AppUser;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface AppUserRepository extends ReactiveCrudRepository<AppUser, Integer> {

    Mono<AppUser> findByEmail(String email);

    Flux<AppUser> findAllByEmailContaining(String partialEmail);

} // The End...
