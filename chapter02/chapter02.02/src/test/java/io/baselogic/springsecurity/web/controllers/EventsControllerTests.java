package io.baselogic.springsecurity.web.controllers;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.html.*;
import com.gargoylesoftware.htmlunit.util.NameValuePair;

import io.baselogic.springsecurity.domain.Event;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MvcResult;
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
import org.springframework.web.servlet.ModelAndView;

import java.util.Calendar;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.*;


// Assert-J: ----------------------------------------------------------------//
// --> assertThat(result.size()).isGreaterThan(0);
// http://joel-costigliola.github.io/assertj/index.html
import static org.assertj.core.api.Assertions.*;

/**
 * Functional and Mock tests for the EventController.
 */
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@SpringBootTest
@Slf4j
public class EventsControllerTests {

    @Autowired
    private MockMvc mockMvc;

    private WebClient webClient;

    private static final String USER = "user";

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

    /**
     * Test the URI for All Events.
     * In this test, BASIC Authentication is activated through
     * auto configuration so there is now a 401 Unauthorized redirect.
     */
    @Test
    @DisplayName("All Events: UnAuthorized - WithAnonymousUser - RequestPostProcessor")
    @WithAnonymousUser
    public void allEvents_not_authenticated__WithAnonymousUser() throws Exception {

        MvcResult result = mockMvc.perform(get("/events/"))
                .andExpect(status().isUnauthorized())
                // The login page should be displayed
                .andReturn();

        String location = result.getResponse().getHeader("Location");
//        assertThat(location).contains("/login/form");
    }

    /**
     * Test the URI for All Events.
     * Using the MockMvc RequestPostProcessor, we set a valid USER {@link User} accessing
     * the URI and returning the All Events page.
     */
    @Test
    @DisplayName("All Events: UnAuthorized - WithUser - RequestPostProcessor")
    public void allEventsPage_not_authenticated__WithUser_rpp() throws Exception {

        MvcResult result = mockMvc.perform(get("/events/")
                // Simulate a valid security User:
                .with(user(USER))
        )
                .andExpect(status().isOk())
                .andExpect(view().name("events/list"))
                .andReturn();

        String content = result.getResponse().getContentAsString();
        assertThat(content).contains("All Event");
        ModelAndView mav = result.getModelAndView();
        List<Event> events = (List<Event>)mav.getModel().get("events");
        assertThat(events.size()).isEqualTo(3);
    }


    //-----------------------------------------------------------------------//
    // All User Events
    //-----------------------------------------------------------------------//

    /**
     * Test the URI for Current User Events with MockMvc.
     * In this test, BASIC Authentication is activated through
     * auto configuration so there is now a 401 Unauthorized redirect.
     */
    @Test
    @DisplayName("Current Users Events - UnAuthorized  - WithAnonymousUser")
    @WithAnonymousUser
    public void testCurrentUsersEventsPage_UnAuthorized() throws Exception {
        MvcResult result = mockMvc.perform(get("/events/my"))
                .andExpect(status().isUnauthorized())
                .andReturn();

//        Collection<String> headers =  result.getResponse().getHeaderNames();
//        log.info("*****");
//        for(String nvp: result.getResponse().getHeaderNames()){
//            log.info("--> {}, {}", nvp, result.getResponse().getHeader(nvp));
//        }
//        log.info("*****");

//        String location = result.getResponse().getHeader("Location");
//        assertThat(location).contains("/login/form");

    }

    /**
     * Test the URI for Current User Events with MockMvc.
     * Using the MockMvc RequestPostProcessor, we set a valid USER {@link User} accessing
     * the URI and returning the Current User Events page.
     */
    @Test
    @DisplayName("Current Users Events")
    public void testCurrentUsersEventsPage__WithUser() throws Exception {
        MvcResult result = mockMvc.perform(get("/events/my")
                // Simulate a valid security User:
                .with(user(USER))
        )
                .andExpect(status().isOk())
                .andExpect(view().name("events/my"))
                .andReturn();

        String content = result.getResponse().getContentAsString();
        assertThat(content).contains("Current User Event");
        // FIXME: Verify the number of events for this user.
        ModelAndView mav = result.getModelAndView();
        List<Event> events = (List<Event>)mav.getModel().get("events");
        assertThat(events.size()).isEqualTo(3);
    }

    /**
     * Test the URI for Current User Events with HtmlUnit.
     * Using the MockMvc RequestPostProcessor, we set a valid USER {@link User} accessing
     * the URI and returning the page.
     */
    @Test
    @DisplayName("Current Users Events - WithUser - HtmlUnit")
    @WithMockUser
    public void testCurrentUsersEventsPage__WithUser_htmlUnit() throws Exception {
        HtmlPage page = webClient.getPage("http://localhost/events/my");

        WebResponse webResponse = page.getWebResponse();

        List<NameValuePair> headers = page.getWebResponse().getResponseHeaders();
//        log.info("*****");
//        for(NameValuePair nvp: headers){
//            log.info("--> {}, {}", nvp.getName(), nvp.getValue());
//        }

        String id = page.getTitleText();
        assertThat(id).isEqualTo("Current Users Events");

        String summary = page.getHtmlElementById("description").getTextContent();
        assertThat(summary).contains("Below you can find the events for");
        assertThat(summary).contains("user1@example.com");

    }

    //-----------------------------------------------------------------------//
    // Events Details
    //-----------------------------------------------------------------------//

    /**
     * Test the URI for showing Event details with MockMvc.
     * Using the MockMvc RequestPostProcessor, we set a valid USER {@link User} accessing
     * the URI and returning the page.
     */
    @Test
    @DisplayName("Show Event Details - WithUser")
    public void testShowEvent_WithUser() throws Exception {
        MvcResult result = mockMvc.perform(get("/events/100")
                // Simulate a valid security User:
                .with(user(USER))
        )
                .andExpect(status().isOk())
                .andExpect(view().name("events/show"))
                .andReturn();

        String content = result.getResponse().getContentAsString();
        assertThat(content).contains("Event Details");

        ModelAndView mav = result.getModelAndView();
        Event event = (Event)mav.getModel().get("event");
        assertThat(event).isNotNull();
        assertThat(event.getSummary()).isEqualTo("Birthday Party");
    }

    //-----------------------------------------------------------------------//
    // Event Form
    //-----------------------------------------------------------------------//

    /**
     * Test the URI for creating a new Event with MockMvc.
     * Using the MockMvc RequestPostProcessor, we set a valid USER {@link User} accessing
     * the URI and returning the page.
     */
    @Test
    @DisplayName("Show Event Form - WithUser")
    public void showEventForm__WithUser() throws Exception {
        MvcResult result = mockMvc.perform(get("/events/form")
                // Simulate a valid security User:
                .with(user(USER))
        )
                .andExpect(status().isOk())
                .andExpect(view().name("events/create"))
                .andReturn();

        String content = result.getResponse().getContentAsString();
        assertThat(content).contains("Create New Event");
    }

    @Test
    @DisplayName("Show Event Form Auto Populate - WithUser")
    @WithMockUser
    public void showEventFormAutoPopulate() throws Exception {
        HtmlPage page = webClient.getPage("http://localhost/events/form");

        HtmlSubmitInput button =  page.getHtmlElementById("auto");
        HtmlForm form =  page.getHtmlElementById("newEventForm");
//        log.info("***: {}", form.asXml());

        HtmlPage pageAfterClick = button.click();

        String titleText = pageAfterClick.getTitleText();
        assertThat(titleText).contains("Create Event");

        String summary = pageAfterClick.getHtmlElementById("summary").asXml();
        assertThat(summary).contains("A new event....");

        String description = pageAfterClick.getHtmlElementById("description").getTextContent();
        assertThat(description).contains("This was auto-populated to save time creating a valid event.");
    }



    @Test
    @DisplayName("Submit Event Form - WithUser")
    @WithMockUser
    public void createEvent() throws Exception {
        HtmlPage page = webClient.getPage("http://localhost/events/form");

        assertThat(page.getTitleText())
                .contains("Create Event");

        HtmlInput email = page.getHtmlElementById("attendeeEmail");
        email.setValueAttribute("user2@example.com");

        HtmlInput when = page.getHtmlElementById("when");
        when.setValueAttribute("2020-07-03 00:00:01");

        HtmlInput summary = page.getHtmlElementById("summary");
        summary.setValueAttribute("Test Summary");

        HtmlTextArea description = page.getHtmlElementById("description");
        description.setText("Test Description");

        HtmlSubmitInput button =  page.getHtmlElementById("submit");


        HtmlPage pageAfterClick = button.click();

        assertThat(pageAfterClick.getTitleText())
                .contains("Current Users Events");
    }

    @Test
    @DisplayName("Submit Event Form - null email - WithUser")
    @WithMockUser
    public void createEvent_null_email() throws Exception {
        HtmlPage page = webClient.getPage("http://localhost/events/form");

        assertThat(page.getTitleText())
                .contains("Create Event");

//        HtmlInput email = page.getHtmlElementById("attendeeEmail");
//        email.setValueAttribute("user2@example.com");

        HtmlInput when = page.getHtmlElementById("when");
        when.setValueAttribute("2020-07-03 00:00:01");

        HtmlInput summary = page.getHtmlElementById("summary");
        summary.setValueAttribute("Test Summary");

        HtmlTextArea description = page.getHtmlElementById("description");
        description.setText("Test Description");

        HtmlSubmitInput button =  page.getHtmlElementById("submit");

        HtmlPage pageAfterClick = button.click();

        assertThat(pageAfterClick.getTitleText())
                .contains("Create Event");

        String errors = pageAfterClick.getHtmlElementById("fieldsErrors").getTextContent();
        assertThat(errors).contains("Attendee Email is required");
    }


    @Test
    @DisplayName("Submit Event Form - not found email - WithUser")
    @WithMockUser
    public void createEvent_not_found_email() throws Exception {
        HtmlPage page = webClient.getPage("http://localhost/events/form");

        assertThat(page.getTitleText())
                .contains("Create Event");

        HtmlInput email = page.getHtmlElementById("attendeeEmail");
        email.setValueAttribute("notfound@example.com");

        HtmlInput when = page.getHtmlElementById("when");
        when.setValueAttribute("2020-07-03 00:00:01");

        HtmlInput summary = page.getHtmlElementById("summary");
        summary.setValueAttribute("Test Summary");

        HtmlTextArea description = page.getHtmlElementById("description");
        description.setText("Test Description");

        HtmlSubmitInput button =  page.getHtmlElementById("submit");

        HtmlPage pageAfterClick = button.click();

        assertThat(pageAfterClick.getTitleText())
                .contains("Create Event");

        String errors = pageAfterClick.getHtmlElementById("fieldsErrors").getTextContent();
        assertThat(errors).contains("Could not find a user for the provided Attendee Email");

    }


    //-------------------------------------------------------------------------

    @Test
    @DisplayName("Submit Event Form - null when - WithUser")
    @WithMockUser
    public void createEvent_null_when() throws Exception {
        HtmlPage page = webClient.getPage("http://localhost/events/form");

        assertThat(page.getTitleText())
                .contains("Create Event");

        HtmlInput email = page.getHtmlElementById("attendeeEmail");
        email.setValueAttribute("user2@example.com");

//        HtmlInput when = page.getHtmlElementById("when");
//        when.setValueAttribute("2020-07-03 00:00:01");

        HtmlInput summary = page.getHtmlElementById("summary");
        summary.setValueAttribute("Test Summary");

        HtmlTextArea description = page.getHtmlElementById("description");
        description.setText("Test Description");

        HtmlSubmitInput button =  page.getHtmlElementById("submit");

        HtmlPage pageAfterClick = button.click();

        assertThat(pageAfterClick.getTitleText())
                .contains("Create Event");

//        log.info("***: {}", pageAfterClick.asXml());

        String errors = pageAfterClick.getHtmlElementById("fieldsErrors").getTextContent();
        assertThat(errors).contains("Event Date/Time is required");

    }


    //-------------------------------------------------------------------------

    @Test
    @DisplayName("Submit Event Form - null summary - WithUser")
    @WithMockUser
    public void createEvent_null_summary() throws Exception {
        HtmlPage page = webClient.getPage("http://localhost/events/form");

        assertThat(page.getTitleText())
                .contains("Create Event");

        HtmlInput email = page.getHtmlElementById("attendeeEmail");
        email.setValueAttribute("user2@example.com");

        HtmlInput when = page.getHtmlElementById("when");
        when.setValueAttribute("2020-07-03 00:00:01");

//        HtmlInput summary = page.getHtmlElementById("summary");
//        summary.setValueAttribute("Test Summary");

        HtmlTextArea description = page.getHtmlElementById("description");
        description.setText("Test Description");

        HtmlSubmitInput button =  page.getHtmlElementById("submit");

        HtmlPage pageAfterClick = button.click();

        assertThat(pageAfterClick.getTitleText())
                .contains("Create Event");

//        log.info("***: {}", pageAfterClick.asXml());

        String errors = pageAfterClick.getHtmlElementById("fieldsErrors").getTextContent();
        assertThat(errors).contains("Summary is required");

    }


    //-------------------------------------------------------------------------

    @Test
    @DisplayName("Submit Event Form - null description - WithUser")
    @WithMockUser
    public void createEvent_null_description() throws Exception {
        HtmlPage page = webClient.getPage("http://localhost/events/form");

        assertThat(page.getTitleText())
                .contains("Create Event");

        HtmlInput email = page.getHtmlElementById("attendeeEmail");
        email.setValueAttribute("user2@example.com");

        HtmlInput when = page.getHtmlElementById("when");
        when.setValueAttribute("2020-07-03 00:00:01");

        HtmlInput summary = page.getHtmlElementById("summary");
        summary.setValueAttribute("Test Summary");

//        HtmlTextArea description = page.getHtmlElementById("description");
//        description.setText("Test Description");

        HtmlSubmitInput button =  page.getHtmlElementById("submit");

        HtmlPage pageAfterClick = button.click();

        assertThat(pageAfterClick.getTitleText())
                .contains("Create Event");

//        log.info("***: {}", pageAfterClick.asXml());


        String errors = pageAfterClick.getHtmlElementById("fieldsErrors").getTextContent();
        assertThat(errors).contains("Description is required");

    }


    //-------------------------------------------------------------------------


} // The End...
