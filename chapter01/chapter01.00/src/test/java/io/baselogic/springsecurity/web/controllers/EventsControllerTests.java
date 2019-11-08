package io.baselogic.springsecurity.web.controllers;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;
import io.baselogic.springsecurity.service.DefaultEventServiceTests;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

//    @LocalServerPort
//    private Integer port;

    @Autowired
    private MockMvc mockMvc;

    private WebClient webClient;

    @BeforeEach
    void setup(WebApplicationContext context) {
        webClient = MockMvcWebClientBuilder
                .webAppContextSetup(context)
                .build();
        webClient.getOptions().setJavaScriptEnabled(false);
        webClient.getOptions().setCssEnabled(false);
    }


    //-------------------------------------------------------------------------

    @Test
    @DisplayName("MockMvc All Events")
    public void allEventsPage() throws Exception {
        mockMvc.perform(get("/events/"))
                .andExpect(status().isOk())
                .andExpect(view().name("events/list"))
                .andReturn();
    }

    @Test
    @DisplayName("MockMvc Current Users Events")
    public void testCurrentUsersEventsPage() throws Exception {

        mockMvc.perform(get("/events/my"))
                .andExpect(status().isOk())
                .andExpect(view().name("events/my"))
                .andReturn();
    }

    @Test
    @DisplayName("HTML Unit Current Users Events")
    public void testCurrentUsersEventsPage_htmlUnit() throws Exception {
        HtmlPage page = webClient.getPage("http://localhost/events/my");

        String id = page.getTitleText();
        assertThat(id).isEqualTo("Current Users Events");

        String summary = page.getHtmlElementById("description").getTextContent();
        assertThat(summary).contains("Below you can find the events for");
        assertThat(summary).contains("user1@example.com");
    }

    @Test
    @DisplayName("Show Event Details")
    public void testShowEvent_htmlUnit() throws Exception {
        HtmlPage page = webClient.getPage("http://localhost/events/100");

        String id = page.getTitleText();
        assertThat(id).contains("Birthday Party");

        String summary = page.getHtmlElementById("summary").getTextContent();
        assertThat(summary).contains("Birthday Party");

        String description = page.getHtmlElementById("description").getTextContent();
        assertThat(description).contains("Time to have my yearly party!");
    }

    @Test
    @DisplayName("Show Event Form")
    public void showEventForm() throws Exception {
        HtmlPage page = webClient.getPage("http://localhost/events/form");

        String titleText = page.getTitleText();
        assertThat(titleText).contains("Create Event");

        String summary = page.getHtmlElementById("legend").getTextContent();
        assertThat(summary).contains("Event Details");
    }

    @Test
    @DisplayName("Show Event Form Auto Populate")
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
    @DisplayName("Submit Event Form")
    public void createEvent() throws Exception {
        HtmlPage page = webClient.getPage("http://localhost/events/form");
//        log.info("***: {}", page.asXml());

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
    @DisplayName("Submit Event Form - null email")
    public void createEvent_null_email() throws Exception {
        HtmlPage page = webClient.getPage("http://localhost/events/form");
//        log.info("***: {}", page.asXml());

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
        log.info("***: {}", pageAfterClick.asXml());

        assertThat(pageAfterClick.getTitleText())
                .contains("Create Event");

        String errors = pageAfterClick.getHtmlElementById("fieldsErrors").getTextContent();
        assertThat(errors).contains("Attendee Email is required");

    }


    //-------------------------------------------------------------------------


} // The End...
