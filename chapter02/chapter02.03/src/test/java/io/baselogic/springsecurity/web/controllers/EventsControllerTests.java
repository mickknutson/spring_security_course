package io.baselogic.springsecurity.web.controllers;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlButton;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
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
import static org.hamcrest.CoreMatchers.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@SpringBootTest
@Slf4j
public class EventsControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserContext userContext;

    private WebClient webClient;

    private static final String USER = TestUtils.APP_USER_1.getEmail();

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
     * In this test, BASIC Authentication is activated through
     * auto configuration so there is now a 401 Unauthorized redirect.
     */
    @Test
    @DisplayName("All Events: UnAuthorized - WithAnonymousUser - RequestPostProcessor")
    @WithAnonymousUser
    public void allEvents_not_authenticated__WithAnonymousUser() throws Exception {

        MvcResult result = mockMvc.perform(get("/events/")
        )
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
    public void allEvents_not_authenticated__WithUser1() throws Exception {
        MvcResult result = mockMvc.perform(get("/events/")
                // Simulate a valid security User:
                .with(user(USER))
        )
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

                .andExpect(content().string(containsString("All Event")))

                .andReturn();

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
    public void testCurrentUsersEventsPage_htmlUnit() throws Exception {

        MvcResult result = mockMvc.perform(get("/events/my")
                // Simulate a valid security User:
                .with(user(USER))
        )
                .andExpect(status().isOk())

                .andExpect(content().string(containsString("Current User Events")))
                .andExpect(content().string(containsString("This shows all events for the current appUser.")))
// FIXME:                .andExpect(model().attribute("events", is()))
                .andReturn();

        ModelAndView mav = result.getModelAndView();
        List<Event> events = (List<Event>)mav.getModel().get("events");
        assertThat(events.size()).isEqualTo(3);


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

        MvcResult result = mockMvc.perform(get("/events/100")
                // Simulate a valid security User:
                .with(user(USER))
        )
                .andExpect(status().isOk())

                .andExpect(content().string(containsString("Birthday Party")))
                .andExpect(content().string(containsString("Time to have my yearly party!")))

                .andReturn();
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

                .andExpect(content().string(containsString("Create New Event")))

                .andReturn();
    }

    @Test
    @DisplayName("Show Event Form Auto Populate")
    @WithMockUser("user1@example.com")
    public void showEventFormAutoPopulate() throws Exception {
        MvcResult result = mockMvc.perform(post("/events/new")
                // Simulate a valid security User:
                .with(user(USER))
                .param("auto", "auto")
        )
                .andExpect(status().isOk())
                .andExpect(view().name("events/create"))

                .andExpect(content().string(containsString("Create New Event")))
                .andExpect(content().string(containsString("A new event....")))
                .andExpect(content().string(containsString("This was auto-populated to save time creating a valid event.")))

                .andReturn();
    }

    @Test
    @DisplayName("Show Event Form Auto Populate - admin1")
    @WithMockUser("admin1@example.com")
    public void showEventFormAutoPopulate_admin1() throws Exception {
        userContext.setCurrentUser(TestUtils.admin1);

        MvcResult result = mockMvc.perform(post("/events/new")
                // Simulate a valid security User:
                .with(user(TestUtils.admin1.getEmail()))
                .param("auto", "auto")
        )
                .andExpect(status().isOk())
                .andExpect(view().name("events/create"))

                .andExpect(content().string(containsString("Create New Event")))
                .andExpect(content().string(containsString("A new event....")))
                .andExpect(content().string(containsString("This was auto-populated to save time creating a valid event.")))

                .andReturn();
    }

    @Test
    @DisplayName("Submit Event Form")
//    @WithMockUser("user1@example.com")
    public void createEvent() throws Exception {

        MvcResult result = mockMvc.perform(post("/events/new")
                // Simulate a valid security User:
                .with(user(USER))

                .param("attendeeEmail", "user2@example.com")
                .param("when", "2020-07-03 00:00:01")
                .param("summary", "Test Summary")
                .param("description", "Test Description")
        )
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/events/my"))
                .andExpect(header().string("Location", "/events/my"))
                .andExpect(flash().attribute("message", "Successfully added the new event"))

                .andReturn();

    }

    @Test
    @DisplayName("Submit Event Form - null email")
    public void createEvent_null_email() throws Exception {

        MvcResult result = mockMvc.perform(post("/events/new")
                        // Simulate a valid security User:
                        .with(user(USER))

//                .param("attendeeEmail", "user2@example.com")
                        .param("when", "2020-07-03 00:00:01")
                        .param("summary", "Test Summary")
                        .param("description", "Test Description")
        )
                .andExpect(status().isOk())
                .andExpect(model().hasErrors())
                .andExpect(model().attributeHasFieldErrors("eventDto", "attendeeEmail"))

                .andReturn();

    }

    @Test
    @DisplayName("Submit Event Form - not found email")
    public void createEvent_not_found_email() throws Exception {

        MvcResult result = mockMvc.perform(post("/events/new")
                // Simulate a valid security User:
                .with(user(USER))
                .param("attendeeEmail", "notFound@example.com")
                .param("when", "2020-07-03 00:00:01")
                .param("summary", "Test Summary")
                .param("description", "Test Description")
        )
                .andExpect(status().isOk())
                .andExpect(model().hasErrors())
                .andExpect(model().attributeHasFieldErrors("eventDto", "attendeeEmail"))

                .andReturn();

    }


    //-------------------------------------------------------------------------

    @Test
    @DisplayName("Submit Event Form - null when")
    public void createEvent_null_when() throws Exception {

        MvcResult result = mockMvc.perform(post("/events/new")
                        // Simulate a valid security User:
                        .with(user(USER))
                        .param("attendeeEmail", "user2@example.com")
//                .param("when", "2020-07-03 00:00:01")
                        .param("summary", "Test Summary")
                        .param("description", "Test Description")
        )
                .andExpect(status().isOk())
                .andExpect(model().hasErrors())
                .andExpect(model().attributeHasFieldErrors("eventDto", "when"))

                .andReturn();

    }


    //-------------------------------------------------------------------------

    @Test
    @DisplayName("Submit Event Form - null summary")
    public void createEvent_null_summary() throws Exception {

        MvcResult result = mockMvc.perform(post("/events/new")
                        // Simulate a valid security User:
                        .with(user(USER))
                        .param("attendeeEmail", "user2@example.com")
                        .param("when", "2020-07-03 00:00:01")
//                .param("summary", "Test Summary")
                        .param("description", "Test Description")
        )
                .andExpect(status().isOk())
                .andExpect(model().hasErrors())
                .andExpect(model().attributeHasFieldErrors("eventDto", "summary"))

                .andReturn();

    }


    //-------------------------------------------------------------------------

    @Test
    @DisplayName("Submit Event Form - null description")
    public void createEvent_null_description() throws Exception {

        MvcResult result = mockMvc.perform(post("/events/new")
                        // Simulate a valid security User:
                        .with(user(USER))
                        .param("attendeeEmail", "notFound@example.com")
                        .param("when", "2020-07-03 00:00:01")
                        .param("summary", "Test Summary")
//                .param("description", "Test Description")
        )
                .andExpect(status().isOk())
                .andExpect(model().hasErrors())
                .andExpect(model().attributeHasFieldErrors("eventDto", "description"))

                .andReturn();

    }

} // The End...
