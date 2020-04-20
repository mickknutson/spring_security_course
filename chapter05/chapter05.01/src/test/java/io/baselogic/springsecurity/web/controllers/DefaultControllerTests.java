package io.baselogic.springsecurity.web.controllers;

import com.gargoylesoftware.htmlunit.WebClient;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.htmlunit.MockMvcWebClientBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * DefaultControllerTests
 *
 * @since chapter01.00
 */
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@SpringBootTest
@Slf4j
public class DefaultControllerTests {

    @Autowired
    private MockMvc mockMvc;

    // HtmlUnit uses the Rhino Engine
    private WebClient webClient;

    /**
     * Customize the WebClient to work with HtmlUnit
     *
     * @param context WebApplicationContext
     */
    @BeforeEach
    void setup(WebApplicationContext context) {

        // https://docs.spring.io/spring-security/site/docs/current/reference/html5/#test-mockmvc-setup
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();

        webClient = MockMvcWebClientBuilder
                .webAppContextSetup(context)
                .build();
        webClient.getOptions().setJavaScriptEnabled(false);
        webClient.getOptions().setCssEnabled(false);
    }


    //-------------------------------------------------------------------------

    @Test
    @DisplayName("Default Controller - user1")
    @WithMockUser("user1@baselogic.com")
    public void defaultRedirect__user1() throws Exception {
        MvcResult result = mockMvc.perform(get("/default"))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/"))
                .andExpect(header().string("Location", "/"))
                .andReturn();

    }

    @Test
    @DisplayName("Default Controller - admin1 - ADMIN role only")
    @WithMockUser(value = "admin1@baselogic.com", roles = {"ADMIN"})
    public void defaultRedirect__admin1() throws Exception {
        MvcResult result = mockMvc.perform(get("/default"))
                .andExpect(status().isForbidden())
                .andReturn();

    }

    @Test
    @DisplayName("Default Controller - admin1 - ADMIN and USER role")
    @WithMockUser(value = "admin1@baselogic.com", roles = {"USER", "ADMIN"})
    public void defaultRedirect__admin1_roles() throws Exception {
        MvcResult result = mockMvc.perform(get("/default"))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/events/"))
                .andExpect(header().string("Location", "/events/"))
                .andReturn();

    }


    @Test
    @WithAnonymousUser
    public void user1_Login() throws Exception {
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
    }

    //-------------------------------------------------------------------------


} // The End...
