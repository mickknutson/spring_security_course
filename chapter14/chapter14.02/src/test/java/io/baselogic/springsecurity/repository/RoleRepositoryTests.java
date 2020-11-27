package io.baselogic.springsecurity.repository;

import io.baselogic.springsecurity.configuration.MongoDataInitializer;
import io.baselogic.springsecurity.domain.Role;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.ArrayList;

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
    void initRepositoryOperations() {
        assertThat(repository).isNotNull();
    }

    @Test
    @DisplayName("test_findById_user1")
    void test_findById_USER() {
        Mono<Role> result = repository.findById(0);

        StepVerifier
                .create(result.log())
                .assertNext(r -> {
                    assertThat(r)
                            .hasFieldOrPropertyWithValue("id", 0)
                            .hasFieldOrPropertyWithValue("name", "ROLE_USER")
                            .hasFieldOrPropertyWithValue("persisted", false)
                            .hasFieldOrPropertyWithValue("isNew", true)
                    ;

                })
                .expectComplete()
                .verify();

	}

	@Test
    @DisplayName("findAll")
	void test_findAll() {
        log.info("*** test_findAll");

        Flux<Role> result = repository.findAll();

        result.doOnEach( r-> log.info("*** Role: {}", r));

        StepVerifier
                .create(repository.findAll())
                .expectNext(Role.builder().id(0).name("ROLE_USER").persisted(false).build())
                .expectNext(Role.builder().id(1).name("ROLE_ADMIN").persisted(false).build())

//                .expectNextMatches(leadImport -> leadImport.getId() == 1L)
//                .expectNext("Spring Boot").expectNext("Spring Web")
//                .recordWith(ArrayList::new)
//                .expectNextCount(2)
//                .assertNext(role -> {
//                    log.info("Role: {}", role);
////                    assertThat(role.getId()).isZero();
////                    assertThat(role.getName()).isEqualTo("ROLE_USER");
//                })
                .expectComplete()
                .verify();

	}

} // The End...
