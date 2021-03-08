package io.baselogic.springsecurity.web.controllers;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import io.baselogic.springsecurity.annotations.WithMockEventUserDetailsUser1;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.htmlunit.MockMvcWebClientBuilder;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.reactive.function.BodyInserters;

import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * WelcomeControllerTests
 *
 * @since chapter03.00 Created
 * @since chapter14.01 Refactored for WebFlux
 */
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@SpringBootTest(properties = "spring.main.web-application-type=reactive")
@AutoConfigureWebTestClient
@Slf4j
class WelcomeControllerTests {

    @Autowired
    private WebTestClient client;


    @Test
    @DisplayName("Welcome Home Page")
    void test_HomePage() {

        EntityExchangeResult result = client.get()
                .uri("/")
                .exchange()
                .expectStatus().isOk()
                .expectBody().returnResult()
                ;

        String body = new String(result.getResponseBodyContent(), StandardCharsets.UTF_8);

        assertThat(body)
                .contains("Welcome to the EventManager!")
                .contains("Chapter 14.01")
                .contains("Spring Reactive Security using Spring Data JPA for Mongo DB")
                .contains("Each chapter will have a slightly different summary depending on what has been done.")
        ;
    }

    @Test
    @DisplayName("Login Page")
    void test_Login() {

        EntityExchangeResult result = client.get()
                .uri("/login")
                .exchange()
                .expectStatus().isOk()
                .expectBody().returnResult()
                ;

        String body = new String(result.getResponseBodyContent(), StandardCharsets.UTF_8);

        assertThat(body)
                .contains("Login to the Event Manager")
                .contains("Login with Username and Password.")
                .contains("Enter credentials below:")
        ;

        /*
        webTestClient.post()
                .uri("/posts")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(post), Post.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(Post.class)
                .value(response -> {
                    assertNotNull(response);
                    Assert.isTrue(response.getId().equals(post.getId()), "id not equal");
                    Assert.isTrue(response.getBody().equals(post.getBody()), "body not equal");
                    Assert.isTrue(response.getTitle().equals(post.getTitle()), "title not equal");
                });
         */
    }

    @Test
    @DisplayName("Login Page - user1")
    void test_Login_user1() {

        EntityExchangeResult result = client.post()
                .uri("/login")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData("username", "user1@baselogic.com")
                        .with("password", "user1"))
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectHeader().location("/default")
                .expectBody().returnResult()
                ;

    }

    @Test
    @DisplayName("Login Page - bad_credentials")
    void test_Login_bad_credentials() {

        EntityExchangeResult result = client.post()
                .uri("/login")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData("username", "BAD@baselogic.com")
                        .with("password", "BAD"))
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectHeader().location("/login?error")
                .expectBody().returnResult()
                ;

    }


} // The End...
