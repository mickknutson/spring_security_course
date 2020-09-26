package io.baselogic.springsecurity.web.controllers;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlButton;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import io.baselogic.springsecurity.dao.EventDao;
import io.baselogic.springsecurity.dao.TestUtils;
import io.baselogic.springsecurity.domain.Event;
import io.baselogic.springsecurity.service.UserContext;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.htmlunit.MockMvcWebClientBuilder;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.ModelAndViewAssert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

/**
 * EventsControllerTests
 * @author mickknutson
 *
 * @since chapter01.00
 */
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@SpringBootTest
@Slf4j
public class EventsControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserContext userContext;

    @Autowired
    private EventDao eventDao;

    private WebClient webClient;

    /**
     * Customize the WebClient to work with HtmlUnit
     *
     * @param context WebApplicationContext
     */
    @BeforeEach
    void setup(WebApplicationContext context) {
        webClient = MockMvcWebClientBuilder
                .webAppContextSetup(context)
                .build();
        webClient.getOptions().setJavaScriptEnabled(false);
        webClient.getOptions().setCssEnabled(false);
        webClient.getOptions().setPrintContentOnFailingStatusCode(true);
    }


    //-----------------------------------------------------------------------//
    // All Events
    //-----------------------------------------------------------------------//


    /**
     * Test the URI for All Events.
     */
    @Test
    @DisplayName("MockMvc All Events")
    public void allEventsPage() throws Exception {

        // MockMvc request and validation
        MvcResult result = mockMvc.perform(get("/events/"))
                .andExpect(status().isOk())
                .andExpect(view().name("events/list"))
                .andReturn();

        // In-line ModelAndViewAssert validation
        ModelAndView mav = result.getModelAndView();

        assertThat(mav).isNotNull();
        assertViewName(mav, "events/list");

        assertModelAttributeAvailable(mav, "events");
        assertAndReturnModelAttributeOfType(mav, "events", List.class);

        List<Event> expectedEvents = eventDao.findAll();
        assertCompareListModelAttribute(mav, "events", expectedEvents);

        String content = result.getResponse().getContentAsString();
        assertThat(content).contains("All Event");

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
                .andExpect(status().isOk())
                .andExpect(view().name("events/my"))
                .andReturn();
    }


    /**
     * Test the URI for Current User Events with HtmlUnit.
     */
    @Test
    @DisplayName("Current Users Events - HtmlUnit")
    public void testCurrentUsersEventsPage_htmlUnit() throws Exception {
        HtmlPage page = webClient.getPage("http://localhost/events/my");

        String id = page.getTitleText();
        assertThat(id).isEqualTo("Current Users Events");

        String summary = page.getHtmlElementById("description").getTextContent();
        assertThat(summary).contains("Below you can find the events for");
        assertThat(summary).contains("user1@baselogic.com");
    }

    //-----------------------------------------------------------------------//
    // Events Details
    //-----------------------------------------------------------------------//

    /**
     * Test the URI for showing Event details with MockMvc.
     */
    @Test
    @DisplayName("Show Event Details - user1")
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
    @DisplayName("Show Event Form")
    public void showEventForm() throws Exception {
        HtmlPage page = webClient.getPage("http://localhost/events/form");

        String titleText = page.getTitleText();
        assertThat(titleText).contains("Create Event");
    }

    @Test
    @DisplayName("Show Event Form Auto Populate")
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
    @DisplayName("Show Event Form Auto Populate - admin1")
    public void showEventFormAutoPopulate_admin1() throws Exception {
        userContext.setCurrentUser(TestUtils.admin1);

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
    public void createEvent() throws Exception {
        HtmlPage page = webClient.getPage("http://localhost/events/form");

        assertThat(page.getTitleText())
                .contains("Create Event");

        HtmlInput email = page.getHtmlElementById("attendeeEmail");
        email.setValueAttribute("user2@baselogic.com");

        HtmlInput when = page.getHtmlElementById("when");
        when.setValueAttribute("2020-07-03 00:00:01");

        HtmlInput summary = page.getHtmlElementById("summary");
        summary.setValueAttribute("Test Summary");

        HtmlInput description = page.getHtmlElementById("description");
        description.setValueAttribute("Test Description");

        HtmlButton button =  page.getHtmlElementById("submit");


        HtmlPage pageAfterClick = button.click();

        assertThat(pageAfterClick.getTitleText())
                .contains("Current Users Events");
    }

    @Test
    @DisplayName("Submit Event Form - null email")
    public void createEvent_null_email() throws Exception {
        HtmlPage page = webClient.getPage("http://localhost/events/form");

        assertThat(page.getTitleText())
                .contains("Create Event");

//        HtmlInput email = page.getHtmlElementById("attendeeEmail");
//        email.setValueAttribute("user2@baselogic.com");

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
        email.setValueAttribute("notfound@baselogic.com");

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
        assertThat(errors).contains("Could not find a appUser for the provided Attendee Email");

    }


    //-----------------------------------------------------------------------//

    @Test
    @DisplayName("Submit Event Form - null when")
    public void createEvent_null_when() throws Exception {
        HtmlPage page = webClient.getPage("http://localhost/events/form");

        assertThat(page.getTitleText())
                .contains("Create Event");

        HtmlInput email = page.getHtmlElementById("attendeeEmail");
        email.setValueAttribute("user2@baselogic.com");

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


    //-----------------------------------------------------------------------//

    @Test
    @DisplayName("Submit Event Form - null summary")
    public void createEvent_null_summary() throws Exception {
        HtmlPage page = webClient.getPage("http://localhost/events/form");

        assertThat(page.getTitleText())
                .contains("Create Event");

        HtmlInput email = page.getHtmlElementById("attendeeEmail");
        email.setValueAttribute("user2@baselogic.com");

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


    //-----------------------------------------------------------------------//

    @Test
    @DisplayName("Submit Event Form - null description")
    public void createEvent_null_description() throws Exception {
        HtmlPage page = webClient.getPage("http://localhost/events/form");

        assertThat(page.getTitleText())
                .contains("Create Event");

        HtmlInput email = page.getHtmlElementById("attendeeEmail");
        email.setValueAttribute("user2@baselogic.com");

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


    //-----------------------------------------------------------------------//


} // The End...
