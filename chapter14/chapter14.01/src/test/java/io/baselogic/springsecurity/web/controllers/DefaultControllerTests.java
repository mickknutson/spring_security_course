package io.baselogic.springsecurity.web.controllers;


import io.baselogic.springsecurity.annotations.WithMockEventUserDetailsAdmin1;
import io.baselogic.springsecurity.annotations.WithMockEventUserDetailsUser1;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * DefaultControllerTests
 *
 * @since chapter02.01 Created
 * @since chapter14.01 Refactored for WebFlux
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
class DefaultControllerTests {

    private WebTestClient client;

    /**
     * Customize the WebClient
     *
     */
    @BeforeEach
    void beforeEachTest(ApplicationContext context) {
        this.client = WebTestClient
                .bindToApplicationContext(context)
                .configureClient()
                .build();
    }


    //-----------------------------------------------------------------------//

    @Test
    @DisplayName("Default Controller - user1")
    @WithMockEventUserDetailsUser1
    void test_defaultRedirect__user1() {

        EntityExchangeResult result = client.get()
                .uri("/default")
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectHeader().location("/")
                .expectBody().returnResult()
                ;
    }


    @Test
    @DisplayName("Default Controller - admin1 - ADMIN and USER role")
    @WithMockEventUserDetailsAdmin1
    void test_defaultRedirect__admin1_roles() throws Exception {

        EntityExchangeResult result = client.get()
                .uri("/default")
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectHeader().location("/events/")
                .expectBody().returnResult()
                ;
    }

} // The End...
