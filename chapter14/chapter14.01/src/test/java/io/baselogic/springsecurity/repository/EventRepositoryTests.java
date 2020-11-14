package io.baselogic.springsecurity.repository;

import io.baselogic.springsecurity.configuration.MongoDataInitializer;
import io.baselogic.springsecurity.dao.TestUtils;
import io.baselogic.springsecurity.domain.Event;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Calendar;

import static org.assertj.core.api.Assertions.assertThat;

/**
 *
 * @author mickknutson
 *
 * @since chapter14.01 Updated for Reactive WebFlux
 */
@DataMongoTest
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
    @DisplayName("test_findAll")
    void test_findAll() {

        Flux<Event> result = repository.findAll();

        StepVerifier
                .create(result.log())
                .expectNextCount(3)
                .expectComplete()
                .verify();
    }


    @Test
    @DisplayName("test_findById")
    void test_findById() {

        Mono<Event> result = repository.findById(100);

        StepVerifier
                .create(result.log())
                .assertNext(r -> {
                    log.info("Result: {} ", r);
                    assertThat(r).isNotNull()
                            .isNotEqualTo(new Object())
                            .hasFieldOrPropertyWithValue("id", 100)
                            .hasFieldOrPropertyWithValue("summary", "Birthday Party")
                            .hasFieldOrPropertyWithValue("summary", "Birthday Party")
                            .hasFieldOrPropertyWithValue("persisted", false)
                            .hasFieldOrPropertyWithValue("isNew", true)
                    ;
                    assertThat(r.hashCode()).isNotZero();
                    assertThat(r.getOwner().getId()).isZero();
                    assertThat(r.getAttendee().getId()).isEqualTo(1);

                }).expectComplete().verify();
    }


    @Test
    @DisplayName("test_findByUser - user1")
    void test_findByOwner_integer() {

        Flux<Event> result = repository.findByOwner(0);

        StepVerifier
                .create(result.log())
                .assertNext(r -> {
                    log.info("r: {} ", r);
                    assertThat(r).isNotNull()
                            .isNotEqualTo(new Object())
                            .hasFieldOrPropertyWithValue("id", 100)
                            .hasFieldOrPropertyWithValue("summary", "Birthday Party");
                })

                .expectComplete().verify();
    }


    /*@Test
    @DisplayName("test_findByOwner_user1_by_Example")
    void test_findByOwner_user1_by_Example() {

        Event example = new Event();
        example.setOwner(TestUtils.user1);

//        Flux<Event> result = repository.findAll();
        Flux<Event> result = repository.findAll(Example.of(example));
//        Flux<Event> result = repository.findByOwner(TestUtils.user1);
//        Flux<Event> result = repository.findByOwner(0);

        StepVerifier
                .create(result.log())
                .expectNextCount(1)
                .assertNext(r -> {
                    log.info("r: {} ", r);
                })

                .expectComplete().verify();
    }*/


    @Test
    @DisplayName("test_saveEvent_with_id")
    void test_saveEvent_with_id() {

        Event event = Event.builder()
                .id(42)
                .summary("Testing Event")
                .description("testing Testing Event")
                .when(Calendar.getInstance())
                .attendee(TestUtils.attendee)
                .owner(TestUtils.owner)
                .build();

        Flux<Event> result = repository.findAll();
        StepVerifier.create(result.log()).expectNextCount(3).expectComplete().verify();

        StepVerifier
                .create(repository.save(event).log())
                .assertNext(r -> {
                    log.info("r: {} ", r);
                    assertThat(r).isNotNull()
                            .isNotEqualTo(new Object())
                            .hasFieldOrPropertyWithValue("id", 42)
                            .hasFieldOrPropertyWithValue("summary", "Testing Event")
                            .hasFieldOrPropertyWithValue("description", "testing Testing Event");

                })
                .expectComplete()
                .verify();

        result = repository.findAll();
        StepVerifier.create(result.log()).expectNextCount(4).expectComplete().verify();
    }

    @Test
    @DisplayName("test_saveEvent_no_id")
    void test_saveEvent_no_id() {

        Event event = TestUtils.createMockEvent(TestUtils.owner, TestUtils.attendee, "Testing Event");

        StepVerifier
                .create(repository.save(event).log())
                .expectError(InvalidDataAccessApiUsageException.class)
                .verify();
    }

    @Test
    @DisplayName("EventRepository - create Event - null event")
    void test_saveEvent_null_event() {
        Event event = TestUtils.createMockEvent(TestUtils.owner, TestUtils.attendee, "Testing Event");

        StepVerifier
                .create(repository.save(event).log())
                .expectError(InvalidDataAccessApiUsageException.class)
                .verify();
    }

    @Test
    @DisplayName("test_saveEvent_null_event_owner")
    void test_saveEvent_null_event_owner() {
        Event event = TestUtils.createMockEvent(TestUtils.owner, TestUtils.attendee, "Testing Event");
        event.setOwner(null);

        StepVerifier
                .create(repository.save(event).log())
                .expectError(InvalidDataAccessApiUsageException.class)
                .verify();
    }

    @Test
    @DisplayName("test_saveEvent_null_event_attendee")
    void test_saveEvent_null_event_attendee() {
        Event event = TestUtils.createMockEvent(TestUtils.owner, TestUtils.attendee, "Testing Event");
        event.setAttendee(null);

        StepVerifier
                .create(repository.save(event).log())
                .expectError(InvalidDataAccessApiUsageException.class)
                .verify();
    }

    @Test
    @DisplayName("test_saveEvent_null_event_when")
    void test_saveEvent_null_event_when() {
        Event event = TestUtils.createMockEvent(TestUtils.owner, TestUtils.attendee, "Testing Event");
        event.setWhen(null);

        StepVerifier
                .create(repository.save(event).log())
                .expectError(InvalidDataAccessApiUsageException.class)
                .verify();
    }


} // The End...
