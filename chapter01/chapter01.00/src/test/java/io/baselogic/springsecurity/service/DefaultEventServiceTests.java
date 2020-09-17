package io.baselogic.springsecurity.service;

import io.baselogic.springsecurity.dao.EventDao;
import io.baselogic.springsecurity.dao.TestUtils;
import io.baselogic.springsecurity.dao.UserDao;
import io.baselogic.springsecurity.domain.AppUser;
import io.baselogic.springsecurity.domain.Event;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.when;
import static org.mockito.Mockito.verify;

/**
 * DefaultEventServiceTests
 *
 * @since chapter01.00
 */
@ExtendWith(SpringExtension.class)
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

    @Test
    public void initJdbcOperations() {
        assertThat(eventService).isNotNull();
    }


    //-----------------------------------------------------------------------//


    @Test
    public void findEventById() {

        // Expectation
        given(eventDao.findById(any(Integer.class)))
                .willReturn(TestUtils.testEvent);


        // Execute test code
        Event event = eventService.findEventById(100);

        // Validate assertions
        assertThat(event).isNotNull();

        verify(eventDao).findById(any(Integer.class));
    }

    @Test
    public void findEventByUser() {

        given(eventDao.findByUser(any(Integer.class)))
                .willReturn(TestUtils.TEST_EVENTS);

        List<Event> events = eventService.findEventByUser(1);

        assertThat(events).isNotEmpty();
        assertThat(events.size()).isGreaterThanOrEqualTo(2);

        verify(eventDao).findByUser(any(Integer.class));
    }

    @Test
    public void findAllEvents() {

        given(eventDao.findAll())
                .willReturn(TestUtils.TEST_EVENTS);

        List<Event> events = eventService.findAllEvents();

        assertThat(events).isNotEmpty();
        assertThat(events.size()).isGreaterThanOrEqualTo(2);

        verify(eventDao).findAll();
    }

    @Test
    public void createEvent() {

        given(eventDao.save(any(Event.class)))
                .willReturn(42);

        int id = eventService.createEvent(Event.builder().build());

        assertThat(id).isEqualTo(42);

        verify(eventDao).save(any(Event.class));
    }

    /*@Test
    public void createEvent_throws_Exception() {

        given(eventDao.save(any(Event.class)))
                .willThrow(new ConstraintViolationException(null));

        assertThrows(ConstraintViolationException.class, () -> {
            eventService.createEvent(null);
        });

        verify(eventDao).save(any(Event.class));
    }*/


    //-----------------------------------------------------------------------//

    @Test
    public void findUserById() {

        when(userDao.findById(any(Integer.class)))
                .thenReturn(TestUtils.TEST_APP_USER_1);

        AppUser appUser = eventService.findUserById(1);

        assertThat(appUser.getEmail()).isEqualTo("test@baselogic.com");

        verify(userDao).findById(1);
    }

    @Test
    public void findUserByEmail() {

        when(userDao.findByEmail(any(String.class)))
                .thenReturn(TestUtils.TEST_APP_USER_1);

        AppUser appUser = eventService.findUserByEmail("test@baselogic.com");

        assertThat(appUser.getEmail()).isEqualTo("test@baselogic.com");

        verify(userDao).findByEmail(any(String.class));
    }

    @Test
    public void findUsersByEmail() {

        when(userDao.findAllByEmail(any(String.class)))
                .thenReturn(TestUtils.TEST_APP_USERS);

        List<AppUser> appUsers = eventService.findUsersByEmail("@baselogic.com");

        assertThat(appUsers).isNotEmpty();
        assertThat(appUsers.size()).isGreaterThanOrEqualTo(3);

        verify(userDao).findAllByEmail("@baselogic.com");
    }

    @Test
    public void createUser() {

        given(userDao.save(any(AppUser.class)))
                .willReturn(42);

        int id = eventService.createUser(TestUtils.testUser1);

        assertThat(id).isEqualTo(42);

        verify(userDao).save(any(AppUser.class));
    }

    /*@Test//(expected = IllegalArgumentException.class)
    public void createUser_with_id() {

        assertThrows(ConstraintViolationException.class, () -> {
            User user = TestUtils.createMockUser("test@baselogic.com", "test", "example");
            user.setId(12345);
            int userId = eventService.createUser(user);
        });

        verify(userDao).save(any(AppUser.class));
    }*/


} // The End...
