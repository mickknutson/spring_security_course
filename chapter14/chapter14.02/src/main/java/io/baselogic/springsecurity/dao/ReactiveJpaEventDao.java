package io.baselogic.springsecurity.dao;

import io.baselogic.springsecurity.domain.AppUser;
import io.baselogic.springsecurity.domain.Event;
import io.baselogic.springsecurity.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * A JPA implementation of {@link EventDao}.
 *
 * @author Mick Knutson
 *
 */
@Repository
public class ReactiveJpaEventDao implements EventDao {

    private final EventRepository eventRepository;

    @Autowired
    public ReactiveJpaEventDao(final @NotNull EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @Override
    public Mono<Event> findById(final @NotNull Integer eventId) {
        return eventRepository.findById(eventId);
    }


    @Override
    public Flux<Event> findByUser(final @NotNull Integer userId) {
        return eventRepository.findByOwner(userId);
    }

    @Override
    public Flux<Event> findAll() {
        return eventRepository.findAll();
    }


    @Override
    public Mono<Integer> save(final @NotNull @Valid Event event) {
        Mono<Event> newEvent = eventRepository.save(event);
        return newEvent.map( e -> e.getId());
    }

} // The End...
