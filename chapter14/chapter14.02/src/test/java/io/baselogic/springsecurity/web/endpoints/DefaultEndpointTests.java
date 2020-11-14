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

import static io.baselogic.springsecurity.ReactiveTestUtils.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.baselogic.springsecurity.annotations.WithMockEventUserDetailsAdmin1;
import io.baselogic.springsecurity.annotations.WithMockEventUserDetailsUser1;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static io.baselogic.springsecurity.ReactiveTestUtils.*;

/**
 * Endpoint to handle after-login behavior
 *
 * @author mickknutson
 *
 * @since chapter02.06 Created class
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
class DefaultEndpointTests {

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


    //-----------------------------------------------------------------------//

    @Test
    @DisplayName("Default Controller - user1")
    @WithMockEventUserDetailsUser1
    void test_defaultRedirect__user1() {

        EntityExchangeResult result = client.get()
                .uri("/default")
                .exchange()
                .expectStatus().isOk()
                .expectBody().returnResult()
                ;

        log.info("*** Result: [{}]", result);
        String body = new String(result.getResponseBodyContent(), StandardCharsets.UTF_8);
        log.info("*** Result body: [{}]", body);

//        assertAll(
//                () -> assertThat(jsonPathContent(body, MESSAGE_PATH)).contains("Welcome to the EventManager!"),
//                () -> assertThat(jsonPathContent(body, SUMMARY_PATH)).contains("Chapter14.01")
//        );

//        MvcResult result = mockMvc.perform(get("/default"))
//                .andExpect(status().isFound())
//                .andExpect(view().name("redirect:/"))
//                .andExpect(header().string("Location", "/"))
//                .andReturn();

    }


    /*@Test
    @DisplayName("Default Controller - admin1 - ADMIN and USER role")
    @WithMockEventUserDetailsAdmin1
    void test_defaultRedirect__admin1_roles() throws Exception {
        MvcResult result = mockMvc.perform(get("/default"))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/events/"))
                .andExpect(header().string("Location", "/events/"))
                .andReturn();

    }*/


    /*@Test
    @WithAnonymousUser
    void test_user1_Login() throws Exception {
        mockMvc.perform(post("/login")
                .accept(MediaType.TEXT_HTML)
                .contentType(
                        MediaType.APPLICATION_FORM_URLENCODED)
                .param("username", "user1@baselogic.com")
                .param("password", "user1")
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/default"))
        ;
    }*/

    //-----------------------------------------------------------------------//


} // The End...
