package io.baselogic.springsecurity.userdetails;

import io.baselogic.springsecurity.dao.UserDao;
import io.baselogic.springsecurity.domain.EventUserDetails;
import io.baselogic.springsecurity.domain.Role;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import io.baselogic.springsecurity.ReactiveTestUtils;
import io.baselogic.springsecurity.dao.TestUtils;
import io.baselogic.springsecurity.domain.Event;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * DefaultEventServiceTests
 *
 * @since chapter03.03
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
class EventUserDetailsServiceTests {

    // Mockito:
    @MockBean
    private UserDao userDao;

    @Autowired
    private EventUserDetailsService service;


    //-----------------------------------------------------------------------//


    @BeforeEach
    void beforeEachTest() {
    }


    @Test
    @DisplayName("initOperations")
    void initOperations() {
        assertThat(service).isNotNull();
    }

    //-----------------------------------------------------------------------//


    @Test
    @DisplayName("loadUserByUsername - user1")
    void test_loadUserByUsername_user1() {

        // Expectation
        given(userDao.findByEmail(any(String.class)))
                .willReturn(
                        ReactiveTestUtils.createMono(TestUtils.testUser1)
                );

        // Execute test code
        Mono<UserDetails> result = service.findByUsername(TestUtils.testUser1.getEmail());

        StepVerifier
                .create(result.log())
                .assertNext(r -> {
                    assertThat(r).isInstanceOf(EventUserDetails.class);
                    assertThat(r.getAuthorities()).isNotEmpty();
                    assertThat(r.isAccountNonExpired()).isTrue();
                    assertThat(r.isAccountNonLocked()).isTrue();
                    assertThat(r.isCredentialsNonExpired()).isTrue();
                    assertThat(r.isEnabled()).isTrue();
                    //assertj
                    assertThat(r.getAuthorities()).isNotEmpty();

                }).expectComplete().verify();

        verify(userDao).findByEmail(any(String.class));
    }

    @Test
    @DisplayName("loadUserByUsername - no User found")
    void test_loadUserByUsername__null_credentials() {


        // Expectation
        given(userDao.findByEmail(any(String.class)))
                .willReturn(ReactiveTestUtils.createEmptyAppUserMono());

        // Execute test code
        Mono<UserDetails> result = service.findByUsername(TestUtils.testUser1.getEmail());

        StepVerifier
                .create(result.log())
                .expectError(UsernameNotFoundException.class)
                .verify();

        verify(userDao).findByEmail(any(String.class));
    }

} // The End...
