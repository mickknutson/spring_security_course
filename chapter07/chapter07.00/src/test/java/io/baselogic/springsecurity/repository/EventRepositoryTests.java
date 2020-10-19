package io.baselogic.springsecurity.repository;

import io.baselogic.springsecurity.dao.TestUtils;
import io.baselogic.springsecurity.domain.AppUser;
import io.baselogic.springsecurity.domain.Event;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ConstraintViolationException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;



@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
class EventRepositoryTests {

    @Autowired
    private EventRepository repository;

    private AppUser owner = new AppUser();
    private AppUser attendee = new AppUser();

    @BeforeEach
    void beforeEachTest() {
        owner.setId(1);
        attendee.setId(0);
    }


    @Test
    @DisplayName("EventRepository - Initialize Repository")
    void initRepositoryOperations() {
        assertThat(repository).isNotNull();
    }

    @Test
    @DisplayName("EventRepository - find all Event")
    void findAll() {
        List<Event> events = repository.findAll();
        log.info("***** Events: {}", events);
        assertThat(events.size()).isGreaterThanOrEqualTo(3);
    }


    @Test
    @DisplayName("EventRepository - find Event by id")
    void find() {
        Event event = repository.findById(100).orElseThrow(RuntimeException::new);
        log.info(event.toString());

        assertThat(event).isNotNull()
                .isNotEqualTo(new Object());
        assertThat(event).isNotEqualTo(new Event());
        assertThat(event.hashCode()).isNotZero();

        assertThat(event.getSummary()).isEqualTo("Birthday Party");
         assertThat(event.getOwner().getId()).isZero();
        assertThat(event.getAttendee().getId()).isEqualTo(1);
    }


    @Test
    @DisplayName("EventRepository - create Event")
    void createEvent() {
        log.debug("******************************");
        List<Event> events = repository.findByOwner(owner);
        assertThat(events.size()).isGreaterThanOrEqualTo(1);

        Event event = TestUtils.createMockEvent(owner, attendee, "Testing Event");
        int eventId = repository.save(event).getId();

        List<Event> newEvents = repository.findByOwner(owner);
        assertThat(newEvents.size()).isGreaterThanOrEqualTo(2);
        // find eventId in List...
//        assertThat(newEvents.get(3)).isEqualTo(3);
    }

    @Test
    @DisplayName("EventRepository - create Event - null event")
    void createEvent_null_event() {
        assertThrows(InvalidDataAccessApiUsageException.class, () -> {
            repository.save(null);
        });
    }

    @Test
    @DisplayName("EventRepository - create Event - IllegalArgumentException > ID")
    void createEvent_with_event_id() {
        List<Event> events = repository.findByOwner(owner);
        assertThat(events.size()).isGreaterThanOrEqualTo(1);

//        assertThrows(IllegalArgumentException.class, () -> {
            Event event = TestUtils.createMockEvent(owner, attendee, "Testing Event");
            event.setId(12345);
            repository.save(event);
//        });
        List<Event> events2 = repository.findByOwner(owner);
        assertThat(events2.size()).isGreaterThanOrEqualTo(2);


    }

    @Test
    @DisplayName("EventRepository - create Event - null Owner")
    void createEvent_null_event_owner() {
        Event event = TestUtils.createMockEvent(owner, attendee, "Testing Event");
        event.setOwner(null);

        assertThrows(ConstraintViolationException.class, () -> {
            repository.save(event);
        });

    }

    @Test
    @DisplayName("EventRepository - create Event - null Attendee")
    void createEvent_null_event_attendee() {
        Event event = TestUtils.createMockEvent(owner, attendee, "Testing Event");
        event.setAttendee(null);

        assertThrows(ConstraintViolationException.class, () -> {
            repository.save(event);
        });

    }


    @Test
    @DisplayName("EventRepository - create Event - null Event Date")
    void createEvent_null_event_when() {
        Event event = TestUtils.createMockEvent(owner, attendee, "Testing Event");
        event.setWhen(null);

        assertThrows(ConstraintViolationException.class, () -> {
            repository.save(event);
        });

    }


} // The End...
