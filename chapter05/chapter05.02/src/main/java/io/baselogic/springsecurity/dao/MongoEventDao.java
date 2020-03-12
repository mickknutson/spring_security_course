package io.baselogic.springsecurity.dao;

import io.baselogic.springsecurity.domain.AppUser;
import io.baselogic.springsecurity.domain.Event;
import io.baselogic.springsecurity.repository.EventRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * A MongoDb Document implementation of {@link EventDao}.
 *
 * @author Mick Knutson
 *
 */
@Repository
@Validated
@Slf4j
public class MongoEventDao implements EventDao {

    private final EventRepository eventRepository;

    // Simple Primary Key Generator
    private final AtomicInteger eventPK = new AtomicInteger(102);


    @Autowired
    public MongoEventDao(final @NotNull EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }


    @Override
//    @Transactional(readOnly = true)
    public Event findById(final @NotNull Integer eventId) {
        return eventRepository.findById(eventId).orElse(null);
    }


    @Override
//    @Transactional(readOnly = true)
    public List<Event> findByUser(final @NotNull Integer userId) {
        Event example = new Event();
        AppUser user = new AppUser();
        user.setId(userId);
        example.setOwner(user);

        return eventRepository.findAll(Example.of(example));
    }


    @Override
//    @Transactional(readOnly = true)
    public List<Event> findAll() {
        return eventRepository.findAll();
    }

    @Override
    public Integer save(final @NotNull @Valid Event event) {

        // Get the next PK instance
        event.setId(eventPK.incrementAndGet());

        Event newEvent = eventRepository.save(event);
        return newEvent.getId();
    }

} // The End...
