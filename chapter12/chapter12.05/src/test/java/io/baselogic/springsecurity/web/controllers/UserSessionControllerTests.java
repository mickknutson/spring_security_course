package io.baselogic.springsecurity.web.controllers;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import io.baselogic.springsecurity.annotations.WithMockEventUserDetailsUser1;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.htmlunit.MockMvcWebClientBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

/**
 * UserSessionControllerTests
 *
 * @since chapter12.05
 */
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@SpringBootTest
@Slf4j
public class UserSessionControllerTests {

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
    @DisplayName("Show UserSessions Form - user1")
    @WithMockEventUserDetailsUser1
    public void show_userSessions__WithUser() throws Exception {

        MvcResult result = mockMvc.perform(get("/user/sessions/"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/sessions"))
                .andReturn();

        List<SessionInformation> sessions = (List<SessionInformation>) result.getModelAndView().getModel().get("userSessions");
        assertThat(sessions).isEmpty() ;
    }

    //-----------------------------------------------------------------------//

    @Test
    @DisplayName("Delete Session Form")
    @WithMockEventUserDetailsUser1
    public void delete_user_session_form() throws Exception {


        // DELETE "/sessions/{sessionId}"
        MvcResult result = mockMvc.perform(get("/user/sessions/"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/sessions"))
                .andReturn();

        List<SessionInformation> sessions = (List<SessionInformation>) result.getModelAndView().getModel().get("userSessions");
        assertThat(sessions).isEmpty();



        HtmlPage page = webClient.getPage("http://localhost/user/sessions/");

        log.info("***: {}", page.asXml());

//        assertThat(page.getTitleText())
//                .contains("My Sessions");
//
//        HtmlButton button =  page.getHtmlElementById("delete");
//
//        HtmlPage pageAfterClick = button.click();
//
//        assertThat(pageAfterClick.getTitleText())
//                .contains("Login to the Event Manager");
//
//        assertThat(pageAfterClick.getDocumentURI()).endsWith("/login/form?expired");



        // Now remove a session:

    }

} // The End...
