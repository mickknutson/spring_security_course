package io.baselogic.springsecurity.dao;

import io.baselogic.springsecurity.domain.AppUser;
import io.baselogic.springsecurity.domain.Event;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
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

/**
 * JpaEventDaoTests
 *
 * @since chapter5.01
 */
    
@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
class JpaEventDaoTests {

    @Autowired
    private EventDao eventDao;

    private AppUser owner = new AppUser();
    private AppUser attendee = new AppUser();

    @BeforeEach
    void beforeEachTest() {
        owner.setId(1);
        attendee.setId(0);
    }


    @Test
    void initJdbcOperations() {
        assertThat(eventDao).isNotNull();
    }

    @Test
    void find() {
        Event event = eventDao.findById(100);
        log.info(event.toString());

        assertThat(event).isNotNull();

        assertThat(event).isNotEqualTo(new Object());
        assertThat(event).isNotEqualTo(new Event());
        assertThat(event.hashCode()).isNotZero();

        assertThat(event.getSummary()).isEqualTo("Birthday Party");
         assertThat(event.getOwner().getId()).isZero();
        assertThat(event.getAttendee().getId()).isEqualTo(1);
    }


    @Test
    void createEvent() {
        log.debug("******************************");
        List<Event> events = eventDao.findByUser(owner.getId());
        assertThat(events.size()).isGreaterThanOrEqualTo(1);

        Event event = TestUtils.createMockEvent(owner, attendee, "Testing Event");
        int eventId = eventDao.save(event);

        List<Event> newEvents = eventDao.findByUser(owner.getId());
        assertThat(newEvents.size()).isGreaterThanOrEqualTo(2);
        // find eventId in List...
//        assertThat(newEvents.get(3)).isEqualTo(3);
    }

    @Test
    void createEvent_null_event() {
        assertThrows(InvalidDataAccessApiUsageException.class, () -> {
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
        Event event = TestUtils.createMockEvent(owner, attendee, "Testing Event");
        event.setOwner(null);

        assertThrows(ConstraintViolationException.class, () -> {
            eventDao.save(event);
        });

    }

    @Test
    void createEvent_null_event_attendee() {
        Event event = TestUtils.createMockEvent(owner, attendee, "Testing Event");
        event.setAttendee(null);

        assertThrows(ConstraintViolationException.class, () -> {
            eventDao.save(event);
        });

    }

    @Test
    void createEvent_null_event_when() {
        Event event = TestUtils.createMockEvent(owner, attendee, "Testing Event");
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
