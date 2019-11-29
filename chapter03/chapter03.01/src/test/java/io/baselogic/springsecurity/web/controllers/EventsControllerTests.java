package io.baselogic.springsecurity.web.controllers;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlButton;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import io.baselogic.springsecurity.domain.Event;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.htmlunit.MockMvcWebClientBuilder;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
                .andExpect(status().isFound())
                .andExpect(header().string("Location", "http://localhost/login/form"))

                // The login page should be displayed
                .andReturn();
    }


    /**
     * Test the URI for All Events.
     */
    @Test
    @DisplayName("MockMvc All Events - user1")
    @WithMockUser("user1@example.com")
    public void allEvents_not_authenticated__WithUser1() throws Exception {
        MvcResult result = mockMvc.perform(get("/events/"))
                .andExpect(status().isForbidden())
                .andReturn();

    }


    /**
     * Test the URI for All Events.
     */
    @Test
    @DisplayName("MockMvc All Events - user1 - ROLE_USER")
    @WithMockUser(username="user1@example.com", roles={"USER"})
    public void allEvents_not_authenticated__WithUser1_and_roles() throws Exception {
        MvcResult result = mockMvc.perform(get("/events/"))
                .andExpect(status().isForbidden())
                .andReturn();

    }


    /**
     * Test the URI for All Events.
     */
    @Test
    @DisplayName("MockMvc All Events - admin1")
    @WithMockUser("admin1@example.com")
    public void allEventsPage() throws Exception {
        MvcResult result = mockMvc.perform(get("/events/"))
                .andExpect(status().isForbidden())
                .andReturn();

    }


    /**
     * Test the URI for All Events.
     */
    @Test
    @DisplayName("MockMvc All Events - admin1 - ROLE_ADMIN")
    @WithMockUser(username="admin1@example.com", roles={"USER","ADMIN"})
    public void allEventsPage__WithUser1_roles() throws Exception {
        MvcResult result = mockMvc.perform(get("/events/"))
                .andExpect(status().isOk())
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
     */
    @Test
    @DisplayName("All Events: UnAuthorized - WithNoUser - RequestPostProcessor")
    public void testCurrentUsersEventsPage() throws Exception {

        MvcResult result = mockMvc.perform(get("/events/my"))
                .andExpect(status().isFound())
                .andExpect(header().string("Location", "http://localhost/login/form"))
                .andReturn();

    }


    /**
     * Test the URI for Current User Events with HtmlUnit.
     */
    @Test
    @DisplayName("Current Users Events - HtmlUnit")
    @WithMockUser("user1@example.com")
    public void testCurrentUsersEventsPage_htmlUnit() throws Exception {
        HtmlPage page = webClient.getPage("http://localhost/events/my");

        if(log.isDebugEnabled()){
            log.info("***: {}", page.asXml());
        }

        String id = page.getTitleText();
        assertThat(id).isEqualTo("Current User Events");

        String chapterHeading = page.getHtmlElementById("chapterHeading").getTextContent();
        assertThat(chapterHeading).isEqualTo("Current User Events");

        String summary = page.getHtmlElementById("summary").getTextContent();
        assertThat(summary).contains("This shows all events for the current user.");
    }

    //-----------------------------------------------------------------------//
    // Events Details
    //-----------------------------------------------------------------------//

    /**
     * Test the URI for showing Event details with MockMvc.
     */
    @Test
    @DisplayName("Show Event Details - user1")
    @WithMockUser("user1@example.com")
    public void testShowEvent_user1() throws Exception {
        HtmlPage page = webClient.getPage("http://localhost/events/100");

        String id = page.getTitleText();
        assertThat(id).contains("Birthday Party");

        String summary = page.getHtmlElementById("summary").getTextContent();
        assertThat(summary).contains("Birthday Party");

        String description = page.getHtmlElementById("description").getTextContent();
        assertThat(description).contains("Time to have my yearly party!");
    }


    //-----------------------------------------------------------------------//
    // Event Form
    //-----------------------------------------------------------------------//

    /**
     * Test the URI for creating a new Event with MockMvc.
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
    @DisplayName("Show Event Form Auto Populate")
    @WithMockUser("user1@example.com")
    public void showEventFormAutoPopulate() throws Exception {
        HtmlPage page = webClient.getPage("http://localhost/events/form");

        HtmlButton button =  page.getHtmlElementById("auto");
        HtmlForm form =  page.getHtmlElementById("newEventForm");

        HtmlPage pageAfterClick = button.click();

        String titleText = pageAfterClick.getTitleText();
        assertThat(titleText).contains("Create Event");

        String summary = pageAfterClick.getHtmlElementById("summary").asXml();
        assertThat(summary).contains("A new event....");

        String description = pageAfterClick.getHtmlElementById("description").asXml();
        assertThat(description).contains("This was auto-populated to save time creating a valid event.");
    }

    @Test
    @DisplayName("Submit Event Form")
    @WithMockUser("user1@example.com")
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

        HtmlInput description = page.getHtmlElementById("description");
        description.setValueAttribute("Test Description");

        HtmlButton button =  page.getHtmlElementById("submit");


        HtmlPage pageAfterClick = button.click();

        assertThat(pageAfterClick.getTitleText())
                .contains("Current User Events");
    }

    @Test
    @DisplayName("Submit Event Form - null email")
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

        HtmlInput description = page.getHtmlElementById("description");
        description.setValueAttribute("Test Description");

        HtmlButton button =  page.getHtmlElementById("submit");

        HtmlPage pageAfterClick = button.click();

        assertThat(pageAfterClick.getTitleText())
                .contains("Create Event");

        String errors = pageAfterClick.getHtmlElementById("fieldsErrors").getTextContent();
        assertThat(errors).contains("Attendee Email is required");

    }

    @Test
    @DisplayName("Submit Event Form - not found email")
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

        HtmlInput description = page.getHtmlElementById("description");
        description.setValueAttribute("Test Description");

        HtmlButton button =  page.getHtmlElementById("submit");

        HtmlPage pageAfterClick = button.click();

        assertThat(pageAfterClick.getTitleText())
                .contains("Create Event");

        String errors = pageAfterClick.getHtmlElementById("fieldsErrors").getTextContent();
        assertThat(errors).contains("Could not find a user for the provided Attendee Email");

    }


    //-------------------------------------------------------------------------

    @Test
    @DisplayName("Submit Event Form - null when")
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

        HtmlInput description = page.getHtmlElementById("description");
        description.setValueAttribute("Test Description");

        HtmlButton button =  page.getHtmlElementById("submit");

        HtmlPage pageAfterClick = button.click();

        assertThat(pageAfterClick.getTitleText())
                .contains("Create Event");

        if(log.isTraceEnabled()){
            log.info("***: {}", pageAfterClick.asXml());
        }

        String errors = pageAfterClick.getHtmlElementById("fieldsErrors").getTextContent();
        assertThat(errors).contains("Event Date/Time is required");

    }


    //-------------------------------------------------------------------------

    @Test
    @DisplayName("Submit Event Form - null summary")
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

        HtmlInput description = page.getHtmlElementById("description");
        description.setValueAttribute("Test Description");

        HtmlButton button =  page.getHtmlElementById("submit");

        HtmlPage pageAfterClick = button.click();

        assertThat(pageAfterClick.getTitleText())
                .contains("Create Event");

        if(log.isTraceEnabled()){
            log.info("***: {}", pageAfterClick.asXml());
        }

        String errors = pageAfterClick.getHtmlElementById("fieldsErrors").getTextContent();
        assertThat(errors).contains("Summary is required");

    }


    //-------------------------------------------------------------------------

    @Test
    @DisplayName("Submit Event Form - null description")
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

//        HtmlInput description = page.getHtmlElementById("description");
//        description.setValueAttribute("Test Description");

        HtmlButton button =  page.getHtmlElementById("submit");

        HtmlPage pageAfterClick = button.click();

        assertThat(pageAfterClick.getTitleText())
                .contains("Create Event");

        if(log.isTraceEnabled()){
            log.info("***: {}", pageAfterClick.asXml());
        }

        String errors = pageAfterClick.getHtmlElementById("fieldsErrors").getTextContent();
        assertThat(errors).contains("Description is required");

    }


    //-------------------------------------------------------------------------


} // The End...
