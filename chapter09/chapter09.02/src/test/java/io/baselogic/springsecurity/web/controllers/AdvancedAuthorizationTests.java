package io.baselogic.springsecurity.web.controllers;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import io.baselogic.springsecurity.annotations.WithMockEventUserDetailsAdmin1;
import io.baselogic.springsecurity.annotations.WithMockEventUserDetailsUser1;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.htmlunit.MockMvcWebClientBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * AdvancedAuthorizationTests
 *
 * @author mickknutson
 * @since chapter09.01 created
 * @since chapter09.01 added Conditional rendering tests for user1 & admin1
 * @since chapter09.02 added Conditional ModelAttribute rendering tests for user1 & admin1
 */
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@SpringBootTest
@Slf4j
public class AdvancedAuthorizationTests {

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
        webClient.getOptions().setPrintContentOnFailingStatusCode(true);
    }


    //-----------------------------------------------------------------------//

    @Test
    @DisplayName("Login as user1 -  No All Events Link")
    @WithMockEventUserDetailsUser1
    public void login_user1_home() throws Exception {
        MvcResult result = mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();

        assertThat(content).doesNotContain("<a id=\"navEventsLink\"");
        assertThat(content).contains("<a id=\"navMyEventsLink\"");

    }

    @Test
    @DisplayName("Login as user1 navigate to All Events - forbidden")
    @WithMockEventUserDetailsUser1
    public void login_user1_events() throws Exception {
        MvcResult result = mockMvc.perform(get("/events/"))
                .andExpect(status().isForbidden())
                .andReturn();
    }

    @Test
    @DisplayName("Login as user1 navigate to Index - with Create Link")
    @WithMockEventUserDetailsUser1
    public void login_user1_index_with_create() throws Exception {
        MvcResult result = mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();

        assertThat(content).contains("Create Event");
    }

    @Test
    @DisplayName("Login as user1 navigate to My Events - with Create Link")
    @WithMockEventUserDetailsUser1
    public void login_user1_myevents_with_create() throws Exception {
        MvcResult result = mockMvc.perform(get("/events/my"))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();

        assertThat(content).contains("Create Event");
    }



    //-----------------------------------------------------------------------//

    @Test
    @DisplayName("Login as admin1 -  WITH All Events Link")
    @WithMockEventUserDetailsAdmin1
    public void login_admin1_home() throws Exception {
        MvcResult result = mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();

        assertThat(content).contains("<a id=\"navEventsLink\"");
        assertThat(content).contains("<a id=\"navMyEventsLink\"");

    }

    @Test
    @DisplayName("Login as user1 navigate to All Events - NOT forbidden")
    @WithMockEventUserDetailsAdmin1
    public void login_admin1_events() throws Exception {
        MvcResult result = mockMvc.perform(get("/events/"))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();

        assertThat(content).contains("<h2 id=\"heading\">All Event</h2>");

    }

    @Test
    @DisplayName("Login as admin1 navigate to Index - with Create Link")
    @WithMockEventUserDetailsAdmin1
    public void login_admin1_index_with_create() throws Exception {
        MvcResult result = mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();

        assertThat(content).doesNotContain("Create Event");
    }

    @Test
    @DisplayName("Login as admin1 navigate to All Events - without Create Link")
    @WithMockEventUserDetailsAdmin1
    public void login_admin1_list_with_create() throws Exception {
        MvcResult result = mockMvc.perform(get("/events/"))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();

        assertThat(content).doesNotContain("Create Event");
    }

    @Test
    @DisplayName("Login as admin1 navigate to My Events - without Create Link")
    @WithMockEventUserDetailsAdmin1
    public void login_admin1_myevents_with_create() throws Exception {
        MvcResult result = mockMvc.perform(get("/events/my"))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();

        assertThat(content).doesNotContain("Create Event");
    }



} // The End...
