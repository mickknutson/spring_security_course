package io.baselogic.springsecurity.repository;

import io.baselogic.springsecurity.domain.AppUser;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Repository
public interface AppUserRepository extends ReactiveMongoRepository<AppUser, Integer> {

    Mono<AppUser> findByEmail(String email);

    Flux<AppUser> findAllByEmailContaining(String partialEmail);

} // The End...
