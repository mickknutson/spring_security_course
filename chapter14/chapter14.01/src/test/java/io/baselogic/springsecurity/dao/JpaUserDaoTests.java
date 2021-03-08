package io.baselogic.springsecurity.dao;

import io.baselogic.springsecurity.domain.AppUser;
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

import static org.assertj.core.api.Assertions.assertThat;

/**
 * JpaEventDaoTests
 *
 * @since chapter05.01 Created
 * @since chapter14.01 Updated for Reactive WebFlux
 */
@SpringBootTest
@Slf4j
class JpaUserDaoTests {

    @Autowired
    private UserDao dao;

    @Autowired
    private AppUserNumberGenerator generator;


    @BeforeEach
    void beforeEachTest() {
    }

    //-----------------------------------------------------------------------//

    @Test
    @DisplayName("initJdbcOperations")
    void initJdbcOperations() {
        assertThat(dao).isNotNull();
    }


    @Test
    @DisplayName("test_findById - user1")
    void test_findById() {

        Mono<AppUser> result = dao.findById(0);

        StepVerifier
                .create(result.log())
                .assertNext(r -> {
                    log.info("Result: {} ", r);
                    assertThat(r).isNotNull()
                            .isNotEqualTo(new Object())
                            .hasFieldOrPropertyWithValue("id", 0)
                            .hasFieldOrPropertyWithValue("email", "user1@baselogic.com");

                }).expectComplete().verify();
    }

    @Test
    @DisplayName("test_findByEmail - user1")
    void test_findByEmail() {
        String username = "user1@baselogic.com";

        Mono<AppUser> result = dao.findByEmail(username);

        StepVerifier
                .create(result.log())
                .assertNext(r -> {
                    log.info("Result: {} ", r);
                    assertThat(r).isNotNull()
                            .isNotEqualTo(new Object())
                            .hasFieldOrPropertyWithValue("id", 0)
                            .hasFieldOrPropertyWithValue("email", "user1@baselogic.com");

                }).expectComplete().verify();
    }


    @Test
    @DisplayName("test_findByEmail_no_results - no results")
    void test_findByEmail_no_results() {
        String username = "foo@baselogic.com";

        Mono<AppUser> result = dao.findByEmail(username);

        StepVerifier
                .create(result.log())
                .expectNextCount(0)
                .expectComplete()
                .verify();
    }



    /*@Test
    @DisplayName("test_findById")
    void test_findByEmail_EmptyResultDataAccessException() {

        // Expectation
        given(appUserRepository.findByEmail(any(String.class)))
                .willThrow(EmptyResultDataAccessException.class);

        assertThrows(EmptyResultDataAccessException.class, () -> {
            userDao.save(owner);
        });

    }*/

    //-----------------------------------------------------------------------//

    @Test
    void test_findAllByEmail() {
        String username = "@baselogic.com";

        Flux<AppUser> result = dao.findAllByEmail(username);

        StepVerifier
                .create(result.log())
                .expectNextCount(3)
                .expectComplete()
                .verify();
    }

    @Test
    void test_findAllByEmail_no_results() {
        String username = "@baselogic.BAD";

        Flux<AppUser> result = dao.findAllByEmail(username);

        StepVerifier
                .create(result.log())
                .expectNextCount(0)
                .expectComplete()
                .verify();
    }

    //-----------------------------------------------------------------------//


    @Test
    @DisplayName("save user - with Id")
    void test_saveUser_with_id() {

        String username = "@baselogic.com";

        Flux<AppUser> result = dao.findAllByEmail(username);
        StepVerifier.create(result.log())
                .expectNextCount(3).as("findAllByEmail");;

        AppUser testUser = TestUtils.testUser1;

        StepVerifier
                .create(dao.save(testUser).log("save"))
                .assertNext(r -> {
                    assertThat(r.getId()).isNotNull()
                            .isNotEqualTo(42)
                            .isEqualTo(generator.getLastGivenNumber());
                    assertThat(r.getPassword())
                            .isNotNull()
                            .isEqualTo(testUser.getPassword());
                })
                .expectComplete()
                .verify();

        Mono<AppUser> user = dao.findById(generator.getLastGivenNumber());
        StepVerifier
                .create(user.log("findById"))
                .assertNext(r -> {
                    assertThat(r).isNotNull()
                            .isNotEqualTo(new Object())
                            .hasFieldOrPropertyWithValue("id", generator.getLastGivenNumber())
                            .hasFieldOrPropertyWithValue("email", "test@baselogic.com")
//                            .hasFieldOrPropertyWithValue("roles.size", 1)
//                            .hasFieldOrPropertyWithValue("roles[0].name", "ROLE_USER")
                    ;

                }).expectComplete().verify();

        result = dao.findAllByEmail(username);
        StepVerifier.create(result.log("findAllByEmail"))
                .expectNextCount(4).as("findAllByEmail");
    }



} // The End...
