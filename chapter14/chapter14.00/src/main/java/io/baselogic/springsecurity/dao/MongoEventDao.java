package io.baselogic.springsecurity.dao;

import io.baselogic.springsecurity.domain.AppUser;
import io.baselogic.springsecurity.domain.Event;
import io.baselogic.springsecurity.repository.EventRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * A MongoDb Document implementation of {@link EventDao}.
 *
 * @author Mick Knutson
 * @since chapter05.02 Created Class
 */
@Repository
@Validated
@Slf4j
public class MongoEventDao implements EventDao {

    private final EventRepository eventRepository;
    private final EventNumberGenerator eventNumberGenerator;


    public MongoEventDao(final EventRepository eventRepository,
                         final EventNumberGenerator eventNumberGenerator) {
        Assert.notNull(eventRepository, "eventRepository cannot be null");
        Assert.notNull(eventNumberGenerator, "eventNumberGenerator cannot be null");

        this.eventRepository = eventRepository;
        this.eventNumberGenerator = eventNumberGenerator;
    }


    @Override
    public Event findById(final @NotNull Integer eventId) {
        return eventRepository.findById(eventId).orElse(null);
    }


    @Override
    public List<Event> findByUser(final @NotNull Integer userId) {
        Event example = new Event();
        AppUser user = new AppUser();
        user.setId(userId);
        example.setOwner(user);

        log.info("findByUser: {}", example);

//        return eventRepository.findAll(Example.of(example));
//        return eventRepository.findAllByOwner(user);
        return eventRepository.findByOwner(userId);
    }


    @Override
    public List<Event> findAll() {
        return eventRepository.findAll();
    }

    @Override
    public Integer save(final @NotNull @Valid Event event) {

        event.setId(eventNumberGenerator.getNextGivenNumber());

        Event newEvent = eventRepository.save(event);
        return newEvent.getId();
    }

} // The End...
