package io.baselogic.springsecurity.dao;

import io.baselogic.springsecurity.domain.AppUser;
import io.baselogic.springsecurity.domain.Event;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.validation.ConstraintViolationException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * JpaEventDaoTests
 *
 * @since chapter05.01 Created
 */

@SpringBootTest
@Slf4j
class JpaEventDaoTests {

    @Autowired
    private EventDao eventDao;

    @BeforeEach
    void beforeEachTest() {}


    @Test
    void initJdbcOperations() {
        assertThat(eventDao).isNotNull();
    }

    @Test
    void find() {
        Event event = eventDao.findById(100);
        log.info(event.toString());

        assertThat(event).isNotNull()
                .isNotEqualTo(new Object())
                .isNotEqualTo(new Event());
        assertThat(event.hashCode()).isNotZero();
        assertThat(event.isNew()).isTrue();

        assertThat(event.getSummary()).isEqualTo("Birthday Party");
        assertThat(event.getOwner().getId()).isZero();
        assertThat(event.getAttendee().getId()).isEqualTo(1);
    }


    @Test
    void createEvent() {
        log.debug("******************************");
        List<Event> events = eventDao.findByUser(TestUtils.owner.getId());
        assertThat(events.size()).isPositive();

        Event event = TestUtils.createMockEvent(TestUtils.owner, TestUtils.attendee, "Testing Event");
        event.setPersisted(true);
        assertThat(event.isNew()).isFalse();

        int eventId = eventDao.save(event);

        List<Event> newEvents = eventDao.findByUser(TestUtils.owner.getId());
        assertThat(newEvents.size()).isGreaterThan(events.size());

        // find eventId in List...
//        assertThat(newEvents.get(eventId).isNew()).isFalse();
    }

    @Test
    void createEvent_null_event() {
        assertThrows(ConstraintViolationException.class, () -> {
            eventDao.save(null);
        });
    }

    /*@Test
    void createEvent_with_event_id() {
        Event event = TestUtils.createMockEvent(owner, attendee, "Testing Event");
        event.setId(12345);

        assertThrows(IllegalArgumentException.class, () -> {
            eventDao.save(event);
        });

    }*/

    @Test
    void createEvent_null_event_owner() {
        Event event = TestUtils.createMockEvent(TestUtils.owner, TestUtils.attendee, "Testing Event");
        event.setOwner(null);

        assertThrows(ConstraintViolationException.class, () -> {
            eventDao.save(event);
        });

    }

    @Test
    void createEvent_null_event_attendee() {
        List<Event> eventsPre = eventDao.findAll();
        assertThat(eventsPre.size()).isGreaterThanOrEqualTo(1);

//        assertThrows(ConstraintViolationException.class, () -> {
        Event event = TestUtils.createMockEvent(TestUtils.owner, TestUtils.attendee, "Testing Event");
        event.setAttendee(null);
        eventDao.save(event);
//        });
        List<Event> eventsPost = eventDao.findAll();
        assertThat(eventsPost.size()).isGreaterThan(eventsPre.size());

    }

    @Test
    void createEvent_null_event_when() {
        Event event = TestUtils.createMockEvent(TestUtils.owner, TestUtils.attendee, "Testing Event");
        event.setWhen(null);

        assertThrows(ConstraintViolationException.class, () -> {
            eventDao.save(event);
        });

    }


    @Test
    void findAll() {
        List<Event> events = eventDao.findAll();
        assertThat(events.size()).isGreaterThanOrEqualTo(3);
    }

} // The End...
