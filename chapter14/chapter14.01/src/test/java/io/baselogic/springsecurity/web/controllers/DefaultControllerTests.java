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
    void test_defaultRedirect__user1() throws Exception {
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

    }


    @Test
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
