package io.baselogic.springsecurity.web.controllers;

import io.baselogic.springsecurity.annotations.WithMockEventUserDetailsUser1;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.web.reactive.server.WebTestClient;

/**
 * WelcomeControllerTests
 *
 * @since chapter03.00 Created
 * @since chapter14.01 Refactored for WebFlux
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
class WelcomeControllerTests {

    private WebTestClient client;

    /**
     * Customize the WebClient to work with HtmlUnit
     *
     */
    @BeforeEach
    void beforeEachTest(ApplicationContext context) {
        this.client = WebTestClient
                .bindToApplicationContext(context)
                .configureClient()
                .build();
    }


    @Test
    @DisplayName("Welcome Home Page")
    @WithMockEventUserDetailsUser1
    void testHomePage() {

//        EntityExchangeResult result = client.get()
//                .uri("/")
//                .exchange()
//                .expectStatus().isOk()
//                .expectBody().returnResult()
//                ;
//
//        log.info("*** Result: {}", result);
//        String body = new String(result.getResponseBodyContent(), StandardCharsets.UTF_8);
//        log.info("*** Result body: {}", body);
//
//        assertAll(
//                () -> assertThat(jsonPathContent(body, MESSAGE_PATH)).contains("Welcome to the EventManager!"),
//                () -> assertThat(jsonPathContent(body, SUMMARY_PATH)).contains("Chapter14.01")
//        );
    }


} // The End...
