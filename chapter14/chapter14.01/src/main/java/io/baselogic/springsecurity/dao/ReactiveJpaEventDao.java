package io.baselogic.springsecurity.dao;

import io.baselogic.springsecurity.domain.Event;
import io.baselogic.springsecurity.repository.EventRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * A ReactiveMongoJPA implementation of {@link EventDao}.
 *
 * @author Mick Knutson
 *
 * @since chapter14.01 Refactored class for WebFlux
 */
@Repository
@Slf4j
public class ReactiveJpaEventDao implements EventDao {

    private final EventRepository eventRepository;
    private final EventNumberGenerator eventNumberGenerator;


    public ReactiveJpaEventDao(final EventRepository eventRepository,
                               final EventNumberGenerator eventNumberGenerator) {
        Assert.notNull(eventRepository, "eventRepository cannot be null");
        Assert.notNull(eventNumberGenerator, "eventNumberGenerator cannot be null");

        this.eventRepository = eventRepository;
        this.eventNumberGenerator = eventNumberGenerator;
    }

    @Override
    public Mono<Event> findById(final Integer eventId) {
        return eventRepository.findById(eventId);
    }


    @Override
    public Flux<Event> findByUser(final Integer userId) {
        return eventRepository.findByOwner(userId);
    }

    @Override
    public Flux<Event> findAll() {
        return eventRepository.findAll();
    }


    @Override
    public Mono<Integer> save(final Event event) {

        event.setId(eventNumberGenerator.getNextGivenNumber());
        log.debug("save event: {}", event);

        Mono<Event> result = eventRepository.save(event);

        return result.map( r -> r.getId())
                .doOnSuccess(e -> log.debug("saved Event: {}", e));
    }

} // The End...
