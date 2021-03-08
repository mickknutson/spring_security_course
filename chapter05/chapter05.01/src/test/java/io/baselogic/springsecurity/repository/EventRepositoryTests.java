package io.baselogic.springsecurity.repository;

import io.baselogic.springsecurity.dao.TestUtils;
import io.baselogic.springsecurity.domain.AppUser;
import io.baselogic.springsecurity.domain.Event;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.InvalidDataAccessApiUsageException;

import javax.validation.ConstraintViolationException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
@Slf4j
class EventRepositoryTests {

    @Autowired
    private EventRepository repository;

    @BeforeEach
    void beforeEachTest() {}


    @Test
    @DisplayName("EventRepository - Initialize Repository")
    void initRepositoryOperations() {
        assertThat(repository).isNotNull();
    }

    @Test
    @DisplayName("EventRepository - find all Event")
    void test_findAll() {
        List<Event> events = repository.findAll();
        log.info("***** Events: {}", events);
        assertThat(events.size()).isGreaterThanOrEqualTo(3);
    }


    @Test
    @DisplayName("EventRepository - find Event by id")
    void test_find() {
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
    void test_createEvent() {
        log.debug("******************************");
        List<Event> events = repository.findByOwner(TestUtils.owner);
        assertThat(events.size()).isPositive();

        Event event = TestUtils.createMockEvent(TestUtils.owner, TestUtils.attendee, "Testing Event");
        int eventId = repository.save(event).getId();

        List<Event> newEvents = repository.findByOwner(TestUtils.owner);
        assertThat(newEvents.size()).isGreaterThan(events.size());
        // find eventId in List...
//        assertThat(newEvents.get(3)).isEqualTo(3);
    }

    @Test
    @DisplayName("EventRepository - create Event - null event")
    void test_createEvent_null_event() {
        assertThrows(InvalidDataAccessApiUsageException.class, () -> {
            repository.save(null);
        });
    }

    @Test
    @DisplayName("EventRepository - create Event - IllegalArgumentException > ID")
    void test_createEvent_with_event_id() {
        List<Event> events = repository.findByOwner(TestUtils.owner);
        assertThat(events.size()).isPositive();

        Event event = TestUtils.createMockEvent(TestUtils.owner, TestUtils.attendee, "Testing Event");
        event.setId(12345);
        repository.save(event);

        List<Event> newEvents = repository.findByOwner(TestUtils.owner);
        assertThat(newEvents.size()).isGreaterThan(events.size());


    }

    @Test
    @DisplayName("EventRepository - create Event - null Owner")
    void test_createEvent_null_event_owner() {
        Event event = TestUtils.createMockEvent(TestUtils.owner, TestUtils.attendee, "Testing Event");
        event.setOwner(null);

        assertThrows(ConstraintViolationException.class, () -> {
            repository.save(event);
        });

    }

    @Test
    @DisplayName("EventRepository - create Event - null Attendee")
    void test_createEvent_null_event_attendee() {
        Event event = TestUtils.createMockEvent(TestUtils.owner, TestUtils.attendee, "Testing Event");
        event.setAttendee(null);

        assertThrows(ConstraintViolationException.class, () -> {
            repository.save(event);
        });

    }

    @Test
    @DisplayName("EventRepository - create Event - null Event Date")
    void test_createEvent_null_event_when() {
        Event event = TestUtils.createMockEvent(TestUtils.owner, TestUtils.attendee, "Testing Event");
        event.setWhen(null);

        assertThrows(ConstraintViolationException.class, () -> {
            repository.save(event);
        });

    }


} // The End...
