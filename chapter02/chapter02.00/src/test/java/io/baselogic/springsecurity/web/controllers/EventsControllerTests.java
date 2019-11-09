package io.baselogic.springsecurity.web.controllers;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.html.*;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import io.baselogic.springsecurity.service.DefaultEventServiceTests;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;


// Spring Test: -------------------------------------------------------------//

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.htmlunit.MockMvcWebClientBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.gargoylesoftware.htmlunit.WebClient;


// Junit 5: -----------------------------------------------------------------//
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Calendar;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.*;


// Assert-J: ----------------------------------------------------------------//
// --> assertThat(result.size()).isGreaterThan(0);
// http://joel-costigliola.github.io/assertj/index.html
import static org.assertj.core.api.Assertions.*;


@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@SpringBootTest
@Slf4j
public class EventsControllerTests {

    @Autowired
    private MockMvc mockMvc;

    private WebClient webClient;

    @BeforeEach
    void setup(WebApplicationContext context) {

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



    //-----------------------------------------------------------------------//
    // All Events
    //-----------------------------------------------------------------------//

    @Test
    @DisplayName("All Events: UnAuthorized - MockMvc-RequestPostProcessor")
    public void allEvents_not_authenticated__rpp() throws Exception {

        mockMvc.perform(get("/events/"))
                .andExpect(status().isUnauthorized())
                .andExpect(header().string("WWW-Authenticate", "Basic realm=\"Realm\""))

                // The login page should be displayed
                .andReturn();
    }

    @Test
    @DisplayName("All Events: UnAuthorized - MockMvc-RequestPostProcessor")
    public void allEventsPage_not_authenticated() throws Exception {

        mockMvc.perform(get("/events/")
                .with(user("user")))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(view().name("events/list"))

                .andExpect(header().string("WWW-Authenticate", "Basic realm=\"Realm\"")
        );
    }


    //-----------------------------------------------------------------------//
    // All User Events
    //-----------------------------------------------------------------------//

    @Test
    @DisplayName("MockMvc Current Users Events")
    public void testCurrentUsersEventsPage() throws Exception {

        mockMvc.perform(get("/events/my"))
                .andExpect(status().isUnauthorized())
                .andReturn();
    }

    @Test
    @DisplayName("HTML Unit Current Users Events")
    public void testCurrentUsersEventsPage_htmlUnit() throws Exception {
        HtmlPage page = webClient.getPage("http://localhost/events/my");

        WebResponse webResponse = page.getWebResponse();
//        log.info("***** {}", webResponse.getContentAsString());

//        log.info("***: {}", page.asXml());

        List<NameValuePair> headers = page.getWebResponse().getResponseHeaders();
        log.info("*****");
        for(NameValuePair nvp: headers){
            log.info("--> {}, {}", nvp.getName(), nvp.getValue());
        }

        String id = page.getTitleText();
        assertThat(id).isEqualTo("Current Users Events");

        String summary = page.getHtmlElementById("description").getTextContent();
        assertThat(summary).contains("Below you can find the events for");
        assertThat(summary).contains("user1@example.com");

    }

    //-----------------------------------------------------------------------//
    // Events Details
    //-----------------------------------------------------------------------//

    @Test
    @DisplayName("Show Event Details")
    public void testShowEvent_htmlUnit() throws Exception {
        mockMvc.perform(get("/events/100"))
                .andExpect(status().isUnauthorized())
                .andReturn();
    }

    //-----------------------------------------------------------------------//
    // Event Form
    //-----------------------------------------------------------------------//

    @Test
    @DisplayName("Show Event Form")
    public void showEventForm() throws Exception {
        mockMvc.perform(get("/events/form"))
                .andExpect(status().isUnauthorized())
                .andReturn();
    }

    @Test
    @DisplayName("Show Event Form Auto Populate")
    public void showEventFormAutoPopulate() throws Exception {
        // noop
    }



    @Test
    @DisplayName("Submit Event Form")
    public void createEvent() throws Exception {
        //noop
    }

    @Test
    @DisplayName("Submit Event Form - null email")
    public void createEvent_null_email() throws Exception {
        // noop

    }


    //-------------------------------------------------------------------------


} // The End...
