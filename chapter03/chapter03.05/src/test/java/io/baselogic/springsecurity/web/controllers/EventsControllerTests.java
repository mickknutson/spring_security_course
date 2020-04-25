package io.baselogic.springsecurity.web.controllers;

import io.baselogic.springsecurity.annotations.WithMockEventUserDetailsAdmin1;
import io.baselogic.springsecurity.annotations.WithMockEventUserDetailsUser1;
import io.baselogic.springsecurity.dao.TestUtils;
import lombok.extern.slf4j.Slf4j;
import org.hamcrest.CoreMatchers;
import org.hamcrest.Matcher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
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

    private static final String USER = TestUtils.user1.getEmail();

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
    @WithMockEventUserDetailsUser1
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
    @WithMockEventUserDetailsUser1
    public void allEvents_not_authenticated__WithUser1_and_roles() throws Exception {
        MvcResult result = mockMvc.perform(get("/events/"))
                .andExpect(status().isForbidden())
                .andReturn();

    }


    /**
     * Test the URI for All Events.
     * Using @WithMockUser("admin1@baselogic.com") to ensure this user does not have ADMIN role
     */
    @Test
    @DisplayName("MockMvc All Events - admin1")
    @WithMockEventUserDetailsUser1
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
    @WithMockEventUserDetailsAdmin1
    public void allEventsPage__WithUser1_roles() throws Exception {
        MvcResult result = mockMvc.perform(get("/events/"))
                .andExpect(status().isOk())

                .andExpect(content().string(containsString("All Event")))
                .andExpect(model().attribute("events", hasSize(greaterThanOrEqualTo(3))))
                .andReturn();
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
    @DisplayName("Current Users Events")
    @WithMockEventUserDetailsUser1
    public void testCurrentUsersEventsPage_htmlUnit() throws Exception {

        MvcResult result = mockMvc.perform(get("/events/my"))
                .andExpect(status().isOk())

                .andExpect(content().string(containsString("Current User Events")))
                .andExpect(content().string(containsString("This shows all events for the current appUser.")))
                .andExpect(model().attribute("events", hasSize(greaterThanOrEqualTo(3))))
                .andReturn();
    }

    //-----------------------------------------------------------------------//
    // Events Details
    //-----------------------------------------------------------------------//

    /**
     * Test the URI for showing Event details with MockMvc.
     */
    @Test
    @DisplayName("Show Event Details - user1")
    @WithMockEventUserDetailsUser1
    public void testShowEvent_user1() throws Exception {

        MvcResult result = mockMvc.perform(get("/events/100"))
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
    @WithMockEventUserDetailsUser1
    public void showEventForm__WithUser() throws Exception {
        MvcResult result = mockMvc.perform(get("/events/form"))
                .andExpect(status().isOk())
                .andExpect(view().name("events/create"))

                .andExpect(content().string(containsString("Create New Event")))

                .andReturn();
    }

    @Test
    @DisplayName("Show Event Form Auto Populate")
    @WithMockEventUserDetailsUser1
    public void showEventFormAutoPopulate() throws Exception {
        MvcResult result = mockMvc.perform(post("/events/new")
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
    @WithMockEventUserDetailsUser1
    public void showEventFormAutoPopulate_admin1() throws Exception {
        MvcResult result = mockMvc.perform(post("/events/new")

                .param("auto", "auto")
        )
                .andExpect(status().isOk())
                .andExpect(view().name("events/create"))

                .andExpect(content().string(containsString("Create New Event")))
                .andExpect(content().string(containsString("A new event....")))
                .andExpect(content().string(containsString("This was auto-populated to save time creating a valid event.")))

                .andReturn();
    }

    private Matcher<String> doesNotContainString(String s) {
        return CoreMatchers.not(containsString(s));
    }


    @Test
    @DisplayName("Submit Event Form")
    @WithMockEventUserDetailsUser1
    public void createEvent() throws Exception {

        MvcResult result = mockMvc.perform(post("/events/new")

                .param("attendeeEmail", "user2@baselogic.com")
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
    @WithMockEventUserDetailsUser1
    public void createEvent_null_email() throws Exception {

        MvcResult result = mockMvc.perform(post("/events/new")

//                .param("attendeeEmail", "user2@baselogic.com")
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
    @WithMockEventUserDetailsUser1
    public void createEvent_not_found_email() throws Exception {

        MvcResult result = mockMvc.perform(post("/events/new")

                .param("attendeeEmail", "notFound@baselogic.com")
                .param("when", "2020-07-03 00:00:01")
                .param("summary", "Test Summary")
                .param("description", "Test Description")
        )
                .andExpect(status().isOk())
                .andExpect(model().hasErrors())
                .andExpect(model().attributeHasFieldErrors("eventDto", "attendeeEmail"))

                .andReturn();

    }


    //-----------------------------------------------------------------------//

    @Test
    @DisplayName("Submit Event Form - null when")
    @WithMockEventUserDetailsUser1
    public void createEvent_null_when() throws Exception {

        MvcResult result = mockMvc.perform(post("/events/new")

                .param("attendeeEmail", "user2@baselogic.com")
//                .param("when", "2020-07-03 00:00:01")
                .param("summary", "Test Summary")
                .param("description", "Test Description")
        )
                .andExpect(status().isOk())
                .andExpect(model().hasErrors())
                .andExpect(model().attributeHasFieldErrors("eventDto", "when"))

                .andReturn();

    }


    //-----------------------------------------------------------------------//

    @Test
    @DisplayName("Submit Event Form - null summary")
    @WithMockEventUserDetailsUser1
    public void createEvent_null_summary() throws Exception {

        MvcResult result = mockMvc.perform(post("/events/new")

                .param("attendeeEmail", "user2@baselogic.com")
                .param("when", "2020-07-03 00:00:01")
//                .param("summary", "Test Summary")
                .param("description", "Test Description")
        )
                .andExpect(status().isOk())
                .andExpect(model().hasErrors())
                .andExpect(model().attributeHasFieldErrors("eventDto", "summary"))

                .andReturn();

    }


    //-----------------------------------------------------------------------//

    @Test
    @DisplayName("Submit Event Form - null description")
    @WithMockEventUserDetailsUser1
    public void createEvent_null_description() throws Exception {

        MvcResult result = mockMvc.perform(post("/events/new")

                .param("attendeeEmail", "notFound@baselogic.com")
                .param("when", "2020-07-03 00:00:01")
                .param("summary", "Test Summary")
//                .param("description", "Test Description")
        )
                .andExpect(status().isOk())
                .andExpect(model().hasErrors())
                .andExpect(model().attributeHasFieldErrors("eventDto", "description"))

                .andReturn();

    }

    //-----------------------------------------------------------------------//

    @Test
    @DisplayName("Validate EventUserDetails")
    public void validate_EventUserDetails() throws Exception {

        assertThat(TestUtils.user1UserDetails.isAccountNonExpired()).isTrue();
        assertThat(TestUtils.user1UserDetails.isAccountNonLocked()).isTrue();
        assertThat(TestUtils.user1UserDetails.isCredentialsNonExpired()).isTrue();
        assertThat(TestUtils.user1UserDetails.isEnabled()).isTrue();
    }

} // The End...
