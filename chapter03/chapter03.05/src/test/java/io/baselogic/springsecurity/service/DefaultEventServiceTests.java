package io.baselogic.springsecurity.service;

import io.baselogic.springsecurity.dao.EventDao;
import io.baselogic.springsecurity.dao.TestUtils;
import io.baselogic.springsecurity.dao.UserDao;
import io.baselogic.springsecurity.domain.Event;
import io.baselogic.springsecurity.domain.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.when;

/**
 * DefaultEventServiceTests
 *
 * @since chapter01.00
 */
@ExtendWith(SpringExtension.class)
@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
public class DefaultEventServiceTests {

    // Mockito:
    @MockBean
    private EventDao eventDao;
    @MockBean
    private UserDao userDao;

    @Autowired
    private EventService eventService;


    //-----------------------------------------------------------------------//

    private User user1 = new User();
    private User testUser1 = new User();


    @BeforeEach
    public void beforeEachTest() {
        user1 = TestUtils.user1;
        testUser1 = TestUtils.testUser1;
    }

    //-------------------------------------------------------------------------

    @Test
    public void initJdbcOperations() {
        assertThat(eventService).isNotNull();
    }


    //-------------------------------------------------------------------------


    @Test
    public void findEventById() {

        // Expectation
        given(eventDao.findById(any(Integer.class)))
                .willReturn(TestUtils.testEvent);

        Event event = eventService.findEventById(100);

        assertThat(event).isNotNull();
    }

    @Test
    public void findEventByUser() {

        given(eventDao.findByUser(any(Integer.class)))
                .willReturn(TestUtils.TEST_EVENTS);

        List<Event> events = eventService.findEventByUser(1);

        assertThat(events).isNotEmpty();
        assertThat(events.size()).isGreaterThanOrEqualTo(2);
    }

    @Test
    public void findAllEvents() {

        given(eventDao.findAll())
                .willReturn(TestUtils.TEST_EVENTS);

        List<Event> events = eventService.findAllEvents();

        assertThat(events).isNotEmpty();
        assertThat(events.size()).isGreaterThanOrEqualTo(2);
    }

    @Test
    public void createEvent() {

        given(eventDao.save(any(Event.class)))
                .willReturn(42);

        int id = eventService.createEvent(Event.builder().build());

        assertThat(id).isEqualTo(42);
    }

    /*@Test
    public void createEvent_throws_Exception() {

        given(eventDao.save(any(Event.class)))
                .willThrow(new ConstraintViolationException(null));


        assertThrows(ConstraintViolationException.class, () -> {
            eventService.createEvent(null);
        });
    }*/


    //-------------------------------------------------------------------------

    @Test
    public void findUserById() {

        when(userDao.findById(any(Integer.class)))
                .thenReturn(TestUtils.testUser1);

        User user = eventService.findUserById(1);

        assertThat(user.getEmail()).isEqualTo("test@example.com");
    }

    @Test
    public void findUserByEmail() {

        when(userDao.findByEmail(any(String.class)))
                .thenReturn(TestUtils.testUser1);

        User user = eventService.findUserByEmail("test@example.com");

        assertThat(user.getEmail()).isEqualTo("test@example.com");
    }

    @Test
    public void findUsersByEmail() {

        when(userDao.findAllByEmail(any(String.class)))
                .thenReturn(TestUtils.TEST_USERS);

        List<User> users = eventService.findUsersByEmail("@example.com");

        assertThat(users).isNotEmpty();
        assertThat(users.size()).isGreaterThanOrEqualTo(3);
    }

    //-------------------------------------------------------------------------

    @Test
    @DisplayName("Create User")
    public void createUser() {

        Integer result = eventService.createUser(testUser1);
        assertThat(result).isGreaterThanOrEqualTo(0);
    }


} // The End...
