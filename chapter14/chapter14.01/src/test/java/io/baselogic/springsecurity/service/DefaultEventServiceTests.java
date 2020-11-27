package io.baselogic.springsecurity.service;

import io.baselogic.springsecurity.ReactiveTestUtils;
import io.baselogic.springsecurity.dao.AppUserNumberGenerator;
import io.baselogic.springsecurity.dao.EventDao;
import io.baselogic.springsecurity.dao.TestUtils;
import io.baselogic.springsecurity.dao.UserDao;
import io.baselogic.springsecurity.domain.AppUser;
import io.baselogic.springsecurity.domain.Event;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Calendar;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

/**
 * DefaultEventServiceTests
 *
 * @since chapter01.00 Created
 * @since chapter14.01 Updated for Reactive WebFlux
 */
@SpringBootTest
@Slf4j
class DefaultEventServiceTests {

    // Mockito:
    @MockBean
    private EventDao eventDao;
    @MockBean
    private UserDao userDao;

    @Autowired
    private EventService service;

    @Autowired
    private AppUserNumberGenerator generator;

    //-----------------------------------------------------------------------//

    @Test
    @DisplayName("initJdbcOperations")
    void initJdbcOperations() {
        assertThat(service).isNotNull();
    }


    @Test
    @DisplayName("test_findEventById")
    void test_findEventById() {

        // Expectation
        given(eventDao.findById(any(Integer.class)))
                .willReturn(
                        ReactiveTestUtils.createMono(TestUtils.testEvent)
                );

        // Execute test code
        Mono<Event> result = service.findEventById(42);

        StepVerifier
                .create(result.log())
                .assertNext(r -> {
                    assertThat(r).isNotNull()
                            .hasFieldOrPropertyWithValue("id", 42)
                            .hasFieldOrPropertyWithValue("owner.id", 1)
                            .hasFieldOrPropertyWithValue("attendee.id", 0)
                    ;
                }).expectComplete().verify();

        verify(eventDao).findById(any(Integer.class));
    }

    @Test
    @DisplayName("test_findEventById_no_results")
    void test_findEventById_no_results() {

        // Expectation
        given(eventDao.findById(any(Integer.class)))
                .willReturn(
                        ReactiveTestUtils.createEmptyEventMono()
                );

        // Execute test code
        Mono<Event> result = service.findEventById(42);

        StepVerifier
                .create(result.log())
                .expectNextCount(0)
                .expectComplete()
                .verify();

        verify(eventDao).findById(any(Integer.class));
    }


    @Test
    @DisplayName("test_findEventByUser")
    void test_findEventByUser() {

        given(eventDao.findByUser(any(Integer.class)))
                .willReturn(
                        ReactiveTestUtils.createFlux(
                                TestUtils.testEvent,
                                TestUtils.testEvent2
                        )
                );

        // Execute test code
        Flux<Event> result = service.findEventByUser(1);

        StepVerifier.create(result.log()).expectNextCount(2).expectComplete().verify();

        StepVerifier
                .create(result.log())
                .assertNext(r -> {
                    log.info("Result: {} ", r);
                    assertThat(r).isNotNull()
                            .isNotEqualTo(new Object())
                            .hasFieldOrPropertyWithValue("id", 42)
                            .hasFieldOrPropertyWithValue("owner.id", 1)
                            .hasFieldOrPropertyWithValue("attendee.id", 0)
                    ;

                })
                .assertNext(r -> {
                    log.info("Result: {} ", r);
                    assertThat(r).isNotNull()
                            .isNotEqualTo(new Object())
                            .hasFieldOrPropertyWithValue("id", 24)
                            .hasFieldOrPropertyWithValue("owner.id", 1)
                            .hasFieldOrPropertyWithValue("attendee.id", 0)
                    ;

                })
                .expectComplete().verify();

        verify(eventDao).findByUser(any(Integer.class));
    }


    @Test
    @DisplayName("test_findEventByUser_no_results")
    void test_findEventByUser_no_results() {

        given(eventDao.findByUser(any(Integer.class)))
                .willReturn(
                        ReactiveTestUtils.createEmptyEventFlux()
                );

        // Execute test code
        Flux<Event> result = service.findEventByUser(1234);

        StepVerifier.create(result.log()).expectNextCount(0).expectComplete().verify();

        verify(eventDao).findByUser(any(Integer.class));
    }

    @Test
    @DisplayName("test_findAllEvents")
    void test_findAllEvents() {

        given(eventDao.findAll())
                .willReturn(
                        ReactiveTestUtils.createFlux(
                                TestUtils.testEvent,
                                TestUtils.testEvent2
                        )
                );

        // Execute test code
        Flux<Event> result = service.findAllEvents();

        StepVerifier.create(result.log()).expectNextCount(2).expectComplete().verify();


        verify(eventDao).findAll();
    }



    @Test
    @DisplayName("test_createEvent_with_id")
    void test_createEvent_with_id() {

        Event event = Event.builder()
                .id(84)
                .summary("Testing Event")
                .description("testing Testing Event")
                .when(Calendar.getInstance())
                .attendee(TestUtils.attendee)
                .owner(TestUtils.owner)
                .build();

        given(eventDao.save(any(Event.class)))
                .willReturn(Mono.just(84)
                );

        given(eventDao.findAll())
                .willReturn(
                        ReactiveTestUtils.createFlux(
                                TestUtils.testEvent,
                                TestUtils.testEvent2
                        )
                );

        Flux<Event> result = service.findAllEvents();
        StepVerifier.create(result.log()).expectNextCount(2).expectComplete().verify();

        StepVerifier
                .create(service.createEvent(event).log())
                .assertNext(r -> {
                    assertThat(r).isEqualTo(84);
                })
                .expectComplete()
                .verify();

    }

    /*@Test
    @DisplayName("TBD")
    void test_createEvent_no_id() {

        Event event = TestUtils.createMockEvent(TestUtils.owner, TestUtils.attendee, "Testing Event");

        given(eventDao.save(any(Event.class)))
                .willThrow(new InvalidDataAccessApiUsageException("test error"));

        Mono<Integer> result = service.createEvent(event);

        StepVerifier
                .create(result.log())
                .expectError(InvalidDataAccessApiUsageException.class)
                .verify();

        verify(eventDao).save(any(Event.class));
    }*/


    /*@Test
    @DisplayName("TBD")
    void test_createEvent_throws_Exception() {

        given(eventDao.save(any(Event.class)))
                .willThrow(new ConstraintViolationException(null));


        assertThrows(ConstraintViolationException.class, () -> {
            eventService.createEvent(null);
        });

        verify(eventDao).save(any(Event.class));
    }*/



    @Test
    @DisplayName("test_findUserById")
    void test_findUserById() {

        given(userDao.findById(any(Integer.class)))
                .willReturn(ReactiveTestUtils.createMono(TestUtils.user1));

//        when(userDao.findById(any(Integer.class)))
//                .thenReturn(ReactiveTestUtils.createMono(TestUtils.user1));

        Mono<AppUser> result = service.findUserById(1);

        StepVerifier
                .create(result.log())
                .assertNext(r -> {
                    log.info("Result: {} ", r);
                    assertThat(r).isNotNull()
                            .hasFieldOrPropertyWithValue("id", 1)
                            .hasFieldOrPropertyWithValue("email", "user1@baselogic.com");

                }).expectComplete().verify();

        verify(userDao).findById(1);
    }

    @Test
    @DisplayName("test_findUserByEmail")
    void test_findUserByEmail() {
        String username = "user1@baselogic.com";

        given(userDao.findByEmail(any(String.class)))
                .willReturn(ReactiveTestUtils.createMono(TestUtils.user1));

        Mono<AppUser> result = service.findUserByEmail(username);

        StepVerifier
                .create(result.log())
                .assertNext(r -> {
                    log.info("Result: {} ", r);
                    assertThat(r).isNotNull()
                            .hasFieldOrPropertyWithValue("id", 1)
                            .hasFieldOrPropertyWithValue("email", "user1@baselogic.com");

                }).expectComplete().verify();

        verify(userDao).findByEmail(any(String.class));
    }

    @Test
    @DisplayName("test_findUsersByEmail")
    void test_findUsersByEmail() {
        String username = "@baselogic.com";

        given(userDao.findAllByEmail(any(String.class)))
                .willReturn(
                        ReactiveTestUtils.createFlux(
                                TestUtils.user1,
                                TestUtils.admin1
                        )
                );

        Flux<AppUser> result = service.findUsersByEmail(username);
        StepVerifier.create(result.log()).expectNextCount(2).expectComplete().verify();

        verify(userDao).findAllByEmail(any(String.class));
    }

    @Test
    @DisplayName("test_createUser")
    void test_createUser() {

        given(userDao.save(any(AppUser.class)))
                .willReturn(Mono.just(TestUtils.TEST_APP_USER_1));

        Mono<AppUser> result = service.createUser(TestUtils.testUser1);

        StepVerifier.create(result.log())
                .expectNextCount(1)
                .expectComplete().verify();

        StepVerifier
                .create(result.log())
                .assertNext(r -> {
                    assertThat(r.getId()).isNotNull()
                            .isEqualTo(42);
                })
                .expectComplete()
                .verify();

        verify(userDao).save(any(AppUser.class));
    }


} // The End...
