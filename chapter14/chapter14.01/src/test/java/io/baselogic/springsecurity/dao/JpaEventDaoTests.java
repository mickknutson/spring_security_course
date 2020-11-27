package io.baselogic.springsecurity.dao;

import io.baselogic.springsecurity.domain.Event;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Calendar;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * JpaEventDaoTests
 *
 * @since chapter05.01 Created
 * @since chapter14.01 Updated for Reactive WebFlux
 */
@SpringBootTest
@Slf4j
class JpaEventDaoTests {

    @Autowired
    private EventDao dao;

    @Autowired
    private EventNumberGenerator generator;


    @BeforeEach
    void beforeEachTest() {
    }

    @Test
    @DisplayName("initJdbcOperations")
    void initJdbcOperations() {
        assertThat(dao).isNotNull();
    }


    @Test
    @DisplayName("test_findAll")
    void test_findAll() {

        Flux<Event> result = dao.findAll();

        StepVerifier
                .create(result.log())
                .expectNextCount(3)
                .expectComplete()
                .verify();
    }


    @Test
    @DisplayName("test_findById - user1")
    void test_findById() {

        Mono<Event> result = dao.findById(100);

        StepVerifier
                .create(result.log())
                .assertNext(r -> {
                    log.info("Result: {} ", r);
                    assertThat(r).isNotNull()
                            .isNotEqualTo(new Object())
                            .hasFieldOrPropertyWithValue("id", 100)
                            .hasFieldOrPropertyWithValue("summary", "Birthday Party")
                            .hasFieldOrPropertyWithValue("owner.id", 0)
                            .hasFieldOrPropertyWithValue("attendee.id", 1)
                    ;

                }).expectComplete().verify();
    }

    @Test
    @DisplayName("test_findByUser - user1")
    void test_findByUser_user1() {

        Flux<Event> result = dao.findByUser(0);

        StepVerifier
                .create(result.log())
                .assertNext(r -> {
                    log.info("Result: {} ", r);
                    assertThat(r).isNotNull()
                            .isNotEqualTo(new Object())
                            .hasFieldOrPropertyWithValue("id", 100)
                            .hasFieldOrPropertyWithValue("summary", "Birthday Party");

                }).expectComplete().verify();
    }


    @Test
    @DisplayName("test_findByUser_no_results - no results")
    void test_findByUser_no_results() {

        Flux<Event> result = dao.findByUser(1234);

        StepVerifier
                .create(result.log())
                .expectNextCount(0)
                .expectComplete()
                .verify();
    }




    @Test
    @DisplayName("test_saveEvent_with_id")
    void test_saveEvent_with_id() {

        Event event = Event.builder()
                .id(42) // does not matter as generator creates ID for all new objects
                .summary("Testing Event")
                .description("testing Testing Event")
                .when(Calendar.getInstance())
                .attendee(TestUtils.attendee)
                .owner(TestUtils.owner)
                .build();

        Flux<Event> result = dao.findAll();
        StepVerifier.create(result.log("findAll-1"))
                .expectNextCount(3).as("findAll-1");

        StepVerifier
                .create(dao.save(event).log("save"))
                .assertNext(r -> {
                    assertThat(r).isNotNull()
                            .isNotEqualTo(42)
                            .isEqualTo(generator.getLastGivenNumber());
                })
                .consumeSubscriptionWith(n -> log.info("n: {}", n))
                .expectComplete()
                .verify();

        StepVerifier
                .create(dao.findById(generator.getLastGivenNumber()).log("findById"))
                .assertNext(r -> {
                    assertThat(r).isNotNull()
                            .isNotEqualTo(new Object())
                            .hasFieldOrPropertyWithValue("id", generator.getLastGivenNumber())
                            .hasFieldOrPropertyWithValue("summary", "Testing Event")
                            .hasFieldOrPropertyWithValue("description", "testing Testing Event")
                            .hasFieldOrPropertyWithValue("owner.id", 1)
                            .hasFieldOrPropertyWithValue("attendee.id", 0)
                    ;

                })
                .expectComplete()
                .verify();

        result = dao.findAll();
        StepVerifier.create(result.log("findAll-2"))
                .expectNextCount(4).as("findAll-2");
    }

} // The End...
