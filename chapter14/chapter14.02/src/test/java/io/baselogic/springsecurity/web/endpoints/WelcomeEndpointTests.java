package io.baselogic.springsecurity.web.endpoints;

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
import static org.junit.jupiter.api.Assertions.assertAll;

import static io.baselogic.springsecurity.ReactiveTestUtils.*;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
class WelcomeEndpointTests {

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

        EntityExchangeResult result = client.get()
                .uri("/")
                .exchange()
                .expectStatus().isOk()
                .expectBody().returnResult()
        ;

        log.info("*** Result: {}", result);
        String body = new String(result.getResponseBodyContent(), StandardCharsets.UTF_8);
        log.info("*** Result body: {}", body);

        assertAll(
                () -> assertThat(jsonPathContent(body, MESSAGE_PATH)).contains("Welcome to the EventManager!"),
                () -> assertThat(jsonPathContent(body, SUMMARY_PATH)).contains("Chapter14.01")
        );
    }


} // The End...
