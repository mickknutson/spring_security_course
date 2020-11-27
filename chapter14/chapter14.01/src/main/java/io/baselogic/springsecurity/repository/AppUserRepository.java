package io.baselogic.springsecurity.repository;

import io.baselogic.springsecurity.domain.AppUser;
import io.baselogic.springsecurity.domain.Event;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface AppUserRepository extends ReactiveMongoRepository<AppUser, Integer> {

    Mono<AppUser> findByEmail(String email);

    Mono<Boolean> existsByEmail(String email);

    Flux<AppUser> findAllByEmailContaining(String partialEmail);

} // The End...
