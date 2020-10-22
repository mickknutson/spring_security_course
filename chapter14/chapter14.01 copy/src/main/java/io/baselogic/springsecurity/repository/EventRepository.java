package io.baselogic.springsecurity.repository;

import io.baselogic.springsecurity.domain.AppUser;
import io.baselogic.springsecurity.domain.Event;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

import java.util.List;

public interface EventRepository extends ReactiveCrudRepository<Event, Integer> {

    Flux<Event> findByOwner(AppUser appUser);

} // The End...
