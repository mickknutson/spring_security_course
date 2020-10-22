package io.baselogic.springsecurity.web.endpoints;

import io.baselogic.springsecurity.ReactiveTestUtils;
import io.baselogic.springsecurity.web.model.CommandDto;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.context.WebApplicationContext;


@SpringBootTest
@AutoConfigureWebTestClient
@Slf4j
class WelcomeEndpointsTests {

    /**
     * Customize the WebClient to work with HtmlUnit
     *
     * @param context WebApplicationContext
     */
    @BeforeEach
    void beforeEachTest(WebApplicationContext context) {

    }


    //-----------------------------------------------------------------------//

    @Test
    @DisplayName("Mock Mvc Welcome Home Page")
    void testHomePage(@Autowired WebTestClient webTestClient) throws Exception {

        //create server and bind to URL
        /*WebTestClient webTestClient = WebTestClient
                .bindToServer()
                .baseUrl("localhost:0")
                .responseTimeout(Duration.ofMinutes(1L))
                .build();

        //post service call
        WebTestClient.ResponseSpec response = webTestClient
                .post()
                .uri("/authenticate")
                .body(BodyInserters.fromValue(EMPTY))
                .exchange();

        //validate response status
        response
                .expectStatus()
                .isOk();
        //parse response object
        response
                .expectBody(CommandDto.class)
                .returnResult()
                .getResponseBody();*/

        webTestClient.get().uri("/")
                .exchange()
                .expectStatus().isOk()
                .expectBody(CommandDto.class)
                .isEqualTo(ReactiveTestUtils.TEST_RESPONSE);

    }

    //-----------------------------------------------------------------------//


} // The End...
