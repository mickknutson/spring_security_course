package io.baselogic.springsecurity.repository;

import io.baselogic.springsecurity.configuration.MongoDataInitializer;
import io.baselogic.springsecurity.dao.MongoEventDao;
import io.baselogic.springsecurity.dao.TestUtils;
import io.baselogic.springsecurity.domain.AppUser;
import io.baselogic.springsecurity.domain.Event;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.domain.Example;
import org.springframework.test.annotation.DirtiesContext;

import javax.validation.ConstraintViolationException;
import java.util.Calendar;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 *
 * @author mickknutson
 *
 * @since chapter14.01 Updated for Reactive WebFlux
 */
@DataMongoTest
@DirtiesContext
@Import({MongoDataInitializer.class})
@Slf4j
class EventRepositoryTests {

    @Autowired
    private EventRepository repository;


    @BeforeEach
    void beforeEachTest() {
    }


    @Test
    @DisplayName("Initialize Repository")
    void initRepositoryOperations() {
        assertThat(repository).isNotNull();
    }

    @Test
    @DisplayName("EventRepository - find all Event")
    void test_findAll() {
        List<Event> events = repository.findAll();
        assertThat(events.size()).isGreaterThanOrEqualTo(3);
    }


    @Test
    @DisplayName("EventRepository - find Event by id")
    void test_find() {
        Event event = repository.findById(100).orElseThrow(RuntimeException::new);
        log.info(event.toString());

        assertThat(event).isNotNull()
                .isNotEqualTo(new Object())
                .isNotEqualTo(new Event());
        assertThat(event.hashCode()).isNotZero();
        assertThat(event.isNew()).isTrue();

        assertThat(event.getSummary()).isEqualTo("Birthday Party");
        assertThat(event.getOwner().getId()).isZero();
        assertThat(event.getAttendee().getId()).isOne();
    }

    /**
     * FIXME: This test does not work using @DataMongoTest
     * but works through {@link MongoEventDao#findByUser(Integer)}
     */
//    @Test
    @DisplayName("EventRepository - create Event")
    void test_createEvent() {

        Event example = new Event();
        example.setOwner(TestUtils.owner);

        repository.findAll().forEach(e -> {
            log.info("**** Event: {} \n", e);
            log.info("summary: {}, owner: {}, attendee: {}",
                    e.getSummary(),
                    e.getOwner().getEmail(),
                    e.getAttendee().getEmail());
        });

        List<Event> events = repository.findAll(Example.of(example));
//        List<Event> events = repository.findAllByOwner(TestUtils.owner);

        assertThat(events.size()).isPositive();

        Event event = TestUtils.createMockEvent(TestUtils.owner, TestUtils.attendee, "Testing Event");
        event.setId(24);
        event.setPersisted(true);

        Event newEvent = repository.save(event);

        assertThat(newEvent).isNotNull();
        assertThat(newEvent.isNew()).isFalse();
        assertThat(newEvent.getId()).isEqualTo(24);

        List<Event> events2 = repository.findAll(Example.of(example));
//        List<Event> events = repository.findAllByOwner(TestUtils.owner);
        assertThat(events2.size()).isGreaterThan(events.size());
    }


    @Test
    @DisplayName("EventRepository - create Event - null event")
    void test_createEvent_null_event() {
        assertThrows(IllegalArgumentException.class, () -> {
            repository.save(null);
        });
    }



    @Test
    @DisplayName("EventRepository - create Event - null Owner")
    void test_createEvent_null_event_owner() {
        Event event = TestUtils.createMockEvent(TestUtils.owner, TestUtils.attendee, "Testing Event");
        event.setOwner(null);

        assertThrows(InvalidDataAccessApiUsageException.class, () -> {
            repository.save(event);
        });

    }

    @Test
    @DisplayName("EventRepository - create Event - null Attendee")
    void test_createEvent_null_event_attendee() {
        Event event = TestUtils.createMockEvent(TestUtils.owner, TestUtils.attendee, "Testing Event");
        event.setAttendee(null);

        assertThrows(InvalidDataAccessApiUsageException.class, () -> {
            repository.save(event);
        });

    }

    @Test
    @DisplayName("EventRepository - create Event - null Event Date")
    void test_createEvent_null_event_when() {
        Event event = TestUtils.createMockEvent(TestUtils.owner, TestUtils.attendee, "Testing Event");
        event.setWhen(null);

        assertThrows(InvalidDataAccessApiUsageException.class, () -> {
            repository.save(event);
        });

    }


} // The End...
