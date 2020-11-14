package io.baselogic.springsecurity.repository;

import io.baselogic.springsecurity.domain.Event;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface EventRepository extends ReactiveMongoRepository<Event, Integer> {

    @Query("{'owner.id' : ?0}")
    Flux<Event> findByOwner(Integer id);


} // The End...
