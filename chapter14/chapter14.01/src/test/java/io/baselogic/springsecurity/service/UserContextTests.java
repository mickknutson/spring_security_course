package io.baselogic.springsecurity.service;

import io.baselogic.springsecurity.ReactiveTestUtils;
import io.baselogic.springsecurity.dao.EventDao;
import io.baselogic.springsecurity.dao.TestUtils;
import io.baselogic.springsecurity.dao.UserDao;
import io.baselogic.springsecurity.domain.AppUser;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;


/**
 * UserContextTests
 *
 * @since chapter01.00
 * @since chapter04.02 Can only setCurrentUser() with a user that exist in the db.
 * @since chapter14.01 Updated for Reactive WebFlux
 */
@SpringBootTest
@Slf4j
class UserContextTests {

    // Mockito:
    @MockBean
    private EventService eventService;

    @Autowired
    private UserContext userContext;

    @BeforeEach
    void beforeEachTest() {
    }


    @Test
    void initJdbcOperations() {
        assertThat(userContext).isNotNull();
    }


    @Test
    @DisplayName("test_getCurrentUser_no_current_users")
    @Order(1)
    void test_getCurrentUser_no_current_users() {

        given(eventService.findUserByEmail(any(String.class)))
                .willReturn(Mono.empty());

        Mono<AppUser> result = userContext.getCurrentUser();

        StepVerifier
                .create(result.log("GETCURRENTUSER"))
                .expectNextCount(0)
                .expectComplete()
                .verify();
    }


    @Test
    @DisplayName("test_setCurrentUser_valid_user")
    @Order(2)
    void test_setCurrentUser_valid_user() {
/*
//        given(userDetailsService.findByUsername(any(String.class)))
//                .willReturn(
//                        ReactiveTestUtils.createUserDetailsMono(TestUtils.user1UserDetails)
//                );

        userContext.setCurrentUser(TestUtils.TEST_APP_USER_1);

        Mono<AppUser> result = userContext.getCurrentUser();
        StepVerifier
                .create(result.log("GETCURRENTUSER"))
                .expectNextCount(1)
                .expectComplete()
                .verify();
                */

    }



    @Test
    @DisplayName("test_getCurrentUser_with_current_users")
    @Order(3)
    void test_getCurrentUser_with_current_users() {


        /*given(eventService.findUserByEmail(any(String.class)))
                .willReturn(Mono.empty());

        Mono<AppUser> result = userContext.getCurrentUser();

        StepVerifier
                .create(result.log("GETCURRENTUSER"))
                .expectNextCount(0)
                .expectComplete()
                .verify();*/

    }

    @Test
    @DisplayName("test_findEventById_no_results")
    void test_setCurrentUser_null_User() {
        assertThrows(NullPointerException.class, () -> {
            userContext.setCurrentUser(null);
        });

    }

    @Test
    @DisplayName("test_findEventById_no_results")
    void test_setCurrentUser_invalid_User() {
        AppUser user = new AppUser();

        assertThrows(IllegalArgumentException.class, () -> {
            userContext.setCurrentUser(user);
        });
    }

    @Test
    @DisplayName("getCurrentUser with a null authentication from SecurityContext")
    void test_getCurrentUser_null_authentication() {
//        ReactiveSecurityContextHolder.clearContext();
//        AppUser result = userContext.getCurrentUser();
//        assertThat(result).isNull();
    }

} // The End...
