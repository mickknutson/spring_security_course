package io.baselogic.springsecurity.dao;

import io.baselogic.springsecurity.domain.AppUser;
import io.baselogic.springsecurity.domain.Event;
import io.baselogic.springsecurity.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

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
public class JpaEventDao implements EventDao {

    private final EventRepository eventRepository;

    @Autowired
    public JpaEventDao(final @NotNull EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Event findById(final @NotNull Integer eventId) {
        return eventRepository.getOne(eventId);
    }


    @Override
    @Transactional(readOnly = true)
    public List<Event> findByUser(final @NotNull Integer userId) {
        Event example = new Event();
        AppUser user = new AppUser();
        user.setId(userId);
        example.setOwner(user);

        return eventRepository.findAll(Example.of(example));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Event> findAll() {
        return eventRepository.findAll();
    }


    @Override
    public Integer save(final @NotNull @Valid Event event) {
        Event newEvent = eventRepository.save(event);
        return newEvent.getId();
    }

} // The End...
