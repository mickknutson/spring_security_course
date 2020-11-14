package io.baselogic.springsecurity.repository;

import io.baselogic.springsecurity.configuration.MongoDataInitializer;
import io.baselogic.springsecurity.dao.TestUtils;
import io.baselogic.springsecurity.domain.AppUser;
import io.baselogic.springsecurity.domain.Event;
import io.baselogic.springsecurity.domain.Role;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Calendar;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author mickknutson
 *
 * @since chapter14.01 Updated for Reactive WebFlux
 */
@DataMongoTest
@Import({MongoDataInitializer.class})
@Slf4j
class RoleRepositoryTests {

    @Autowired
    private RoleRepository repository;

    @Test
    @DisplayName("Initialize Repository")
    @Order(1)
    void initRepositoryOperations() {
        assertThat(repository).isNotNull();
    }

    @Test
    @DisplayName("test_findById_user1")
    @Order(2)
    void test_findById_USER() {
        Mono<Role> result = repository.findById(0);

        StepVerifier
                .create(result)
                .assertNext(r -> {
                    assertThat(r)
                            .hasFieldOrPropertyWithValue("id", 0)
                            .hasFieldOrPropertyWithValue("name", "ROLE_USER")
                            .hasFieldOrPropertyWithValue("persisted", false)
                            .isInstanceOf(Role.class);
                    assertThat(r.isNew())
                            .isTrue();

                })
                .expectComplete()
                .verify();

	}

	@Test
    @DisplayName("findAll")
    @Order(3)
	void test_findAll() {
        log.info("*** test_findAll");

        Flux<Role> result = repository.findAll();

        result.doOnEach( r-> log.info("*** Role: {}", r));

        StepVerifier
                .create(repository.findAll())
                .expectNext(Role.builder().id(0).name("ROLE_USER").persisted(false).build())
                .expectNext(Role.builder().id(1).name("ROLE_ADMIN").persisted(false).build())
                .expectComplete()
                .verify();

	}

    @Test
    @DisplayName("test_saveRole_with_id")
    @Order(4)
    void test_saveRole_with_id() {

        Role role = Role.builder()
                .id(42)
                .name("ROLE_TEST")
                .persisted(true)
                .build();

        Flux<Role> result = repository.findAll();

        StepVerifier.create(result)
                .expectNextCount(2).expectComplete().verify();

        StepVerifier
                .create(repository.save(role))
                .assertNext(r -> {
                    log.debug("r: {} ", r);
                    assertThat(r).isNotNull()
                            .isNotEqualTo(new Object())
                            .hasFieldOrPropertyWithValue("id", 42)
                            .hasFieldOrPropertyWithValue("name", "ROLE_TEST");
                    assertThat(r.isNew()).isFalse();

                })
                .expectComplete()
                .verify();

        result = repository.findAll();
        StepVerifier.create(result)
                .expectNextCount(3).expectComplete().verify();
    }


} // The End...
