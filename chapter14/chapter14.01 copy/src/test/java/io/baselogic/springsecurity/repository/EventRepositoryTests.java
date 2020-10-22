package io.baselogic.springsecurity.repository;

import io.baselogic.springsecurity.dao.TestUtils;
import io.baselogic.springsecurity.domain.AppUser;
import io.baselogic.springsecurity.domain.Event;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.ConstraintViolationException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataR2dbcTest
@Slf4j
class EventRepositoryTests {

    @Autowired
    private EventRepository repository;

    @BeforeEach
    void beforeEachTest() {
    }


    /*@Test
    @DisplayName("EventRepository - Initialize Repository")
    void initRepositoryOperations() {
        assertThat(repository).isNotNull();
    }

    @Test
    @DisplayName("EventRepository - find all Event")
    void findAll() {
        Flux<Event> events = repository.findAll();
        log.info("***** Events: {}", events);
        assertThat(events.count().block()).isGreaterThanOrEqualTo(3);
    }


    @Test
    @DisplayName("EventRepository - find Event by id")
    void find() {
        Mono<Event> result = repository.findById(100);
        Event event = result.block();
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
        Flux<Event> events = repository.findByOwner(TestUtils.owner);
        assertThat(events.count().block()).isPositive();

        Event event = TestUtils.createMockEvent(TestUtils.owner, TestUtils.attendee, "Testing Event");
        Mono<Integer> eventId = repository.save(event).map( e -> e.getId());

        Flux<Event> newEvents = repository.findByOwner(TestUtils.owner);
        assertThat(newEvents.count().block()).isGreaterThanOrEqualTo(2);
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
        Flux<Event> events = repository.findByOwner(TestUtils.owner);
        assertThat(events.count().block()).isPositive();

            Event event = TestUtils.createMockEvent(TestUtils.owner, TestUtils.attendee, "Testing Event");
            event.setId(12345);
            repository.save(event);
        Flux<Event> events2 = repository.findByOwner(TestUtils.owner);
        assertThat(events2.count().block()).isGreaterThanOrEqualTo(2);


    }

    @Test
    @DisplayName("EventRepository - create Event - null Owner")
    void createEvent_null_event_owner() {
        Event event = TestUtils.createMockEvent(TestUtils.owner, TestUtils.attendee, "Testing Event");
        event.setOwner(null);

        assertThrows(ConstraintViolationException.class, () -> {
            repository.save(event);
        });

    }

    @Test
    @DisplayName("EventRepository - create Event - null Attendee")
    void createEvent_null_event_attendee() {
        Event event = TestUtils.createMockEvent(TestUtils.owner, TestUtils.attendee, "Testing Event");
        event.setAttendee(null);

        assertThrows(ConstraintViolationException.class, () -> {
            repository.save(event);
        });

    }

    @Test
    @DisplayName("EventRepository - create Event - null Event Date")
    void createEvent_null_event_when() {
        Event event = TestUtils.createMockEvent(TestUtils.owner, TestUtils.attendee, "Testing Event");
        event.setWhen(null);

        assertThrows(ConstraintViolationException.class, () -> {
            repository.save(event);
        });

    }*/


} // The End...
