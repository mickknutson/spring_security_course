package io.baselogic.springsecurity.web.controllers;

import com.gargoylesoftware.htmlunit.WebClient;
import io.baselogic.springsecurity.annotations.WithMockEventUserDetailsUser1;
import io.baselogic.springsecurity.dao.TestUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.htmlunit.MockMvcWebClientBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * UserSessionControllerTests
 *
 * @since chapter12.05
 */
@SpringBootTest
@AutoConfigureMockMvc
//@WebMvcTest(controllers = UserSessionController.class)
@Slf4j
public class UserSessionControllerTests {

    @Autowired
    private MockMvc mockMvc;

    // HtmlUnit uses the Rhino Engine
    private WebClient webClient;

    @MockBean
    private SessionRegistry sessionRegistry;

    /**
     * Customize the WebClient to work with HtmlUnit
     *
     * @param context WebApplicationContext
     */
    @BeforeEach
    void beforeEachTest(WebApplicationContext context) {

        // https://docs.spring.io/spring-security/site/docs/current/reference/html5/#test-mockmvc-setup
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();

        webClient = MockMvcWebClientBuilder
                .webAppContextSetup(context, springSecurity())
                .build();
        webClient.getOptions().setJavaScriptEnabled(false);
        webClient.getOptions().setCssEnabled(false);
        webClient.getOptions().setPrintContentOnFailingStatusCode(true);
    }



    //-----------------------------------------------------------------------//
    // Registration Form
    //-----------------------------------------------------------------------//

    @Test
    @DisplayName("Test Constructore Injection with Null SessionRegistry")
    @WithMockEventUserDetailsUser1
    void testConstructorInjection_null() throws Exception {

        assertThrows(IllegalArgumentException.class, () -> {
            new UserSessionController(null);
        });
    }


    @Test
    @DisplayName("Show UserSessions - user1")
    @WithMockEventUserDetailsUser1
    void show_userSessions__WithUser() throws Exception {

        List<SessionInformation> userSessions = new ArrayList<>(4);
        userSessions.add(new SessionInformation(TestUtils.user1UserDetails, "session-1", new Date()));
        userSessions.add(new SessionInformation(TestUtils.user1UserDetails, "session-2", new Date()));
        userSessions.add(new SessionInformation(TestUtils.user1UserDetails, "session-3", new Date()));
        userSessions.add(new SessionInformation(TestUtils.user1UserDetails, "session-4", new Date()));

        // Expectation
        // SecurityContext:
        given(this.sessionRegistry.getAllSessions(any(Principal.class), anyBoolean()))
                .willReturn(userSessions);


        MvcResult result = mockMvc.perform(get("/user/sessions/"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/sessions"))
                .andReturn();

        List<SessionInformation> sessions = (List<SessionInformation>) result.getModelAndView().getModel().get("userSessions");

        assertThat(sessions).isEqualTo(userSessions);

    }


//    @Test
    @DisplayName("Show UserSessions - null Authentication")
//    @WithAnonymousUser
//    @WithUserDetails(user = null)
    void show_userSessions__null_authentication() throws Exception {

        MvcResult result = mockMvc.perform(get("/user/sessions/"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/error"))
                .andExpect(flash().attribute("message", "Authentication is null"))
                .andReturn();

    }

    //-----------------------------------------------------------------------//

    @Test
    @DisplayName("Delete Session Form")
    @WithMockEventUserDetailsUser1
    public void delete_user_session_form() throws Exception {

        final String sessionId = "session-1";
        final SessionInformation userSession = new SessionInformation(TestUtils.user1UserDetails, "session-1", new Date());

        // Expectation
        // SecurityContext:
        given(this.sessionRegistry.getSessionInformation(sessionId))
                .willReturn(userSession);

        // DELETE "/sessions/{sessionId}"
        MvcResult result = mockMvc.perform(delete("/user/sessions/"+sessionId))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/user/sessions/"))
                .andExpect(flash().attribute("message", "Session was removed"))
                .andReturn();

    }

} // The End...
