package io.baselogic.springsecurity.web.controllers;


import io.baselogic.springsecurity.annotations.WithMockEventUserDetailsAdmin1;
import io.baselogic.springsecurity.annotations.WithMockEventUserDetailsUser1;
import io.baselogic.springsecurity.dao.EventDao;
import io.baselogic.springsecurity.dao.TestUtils;
import io.baselogic.springsecurity.service.EventService;
import io.baselogic.springsecurity.web.model.EventDto;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.BodyInserters;

import java.nio.charset.StandardCharsets;
import java.util.Calendar;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * RegistrationControllerTests
 *
 * @since chapter03.00 Created
 * @since chapter14.01 Refactored for WebFlux
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
class EventsControllerTests {

    private WebTestClient client;

    /**
     * Customize the WebClient
     *
     * @param context WebApplicationContext
     */
    @BeforeEach
    void beforeEachTest(ApplicationContext context) {

        this.client = WebTestClient
                .bindToApplicationContext(context)
                .configureClient()
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
    @DisplayName("All Events: UnAuthorized - WithAnonymousUser")
    @WithAnonymousUser
    void allEvents_not_authenticated__WithAnonymousUser() {

        EntityExchangeResult result = client.get().uri("/events/")
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectHeader().location("/error/403")
                .expectBody().returnResult()
                ;

    }


    /**
     * Test the URI for All Events.
     */
    @Test
    @DisplayName("MockMvc All Events - user1")
    @WithMockEventUserDetailsUser1
    void allEvents_not_authenticated__WithUser1() {

        EntityExchangeResult result = client.get().uri("/events/")
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectHeader().location("/error/403")
                .expectBody().returnResult()
                ;
    }


    /**
     * Test the URI for All Events.
     * Using @WithMockUser("admin1@baselogic.com") to ensure this user does not have ADMIN role
     */
    @Test
    @DisplayName("MockMvc All Events - admin1 with no roles")
    @WithMockUser("admin1@baselogic.com")
    void allEventsPage() {

    EntityExchangeResult result = client.get().uri("/events/")
            .exchange()
            .expectStatus().is3xxRedirection()
            .expectHeader().location("/error/403")
            .expectBody().returnResult()
            ;
    }


    /**
     * Test the URI for All Events.
     */
    @Test
    @DisplayName("MockMvc All Events - admin1 - ROLE_ADMIN")
    @WithMockEventUserDetailsAdmin1
    void allEventsPage__WithUser1_roles() {

        EntityExchangeResult result = client.get().uri("/events/")
                .exchange()
                .expectStatus().isOk()
                .expectBody().returnResult()
                ;

        String body = new String(result.getResponseBodyContent(), StandardCharsets.UTF_8);

        assertThat(body)
                .contains("<a href=\"/events/100\">Birthday Party</a>")
                .contains("<a href=\"/events/101\">Mountain Bike Race</a>")
                .contains("<a href=\"/events/102\">Lunch</a>")
        ;
    }


    //-----------------------------------------------------------------------//
    // All User Events
    //-----------------------------------------------------------------------//

    /**
     * Test the URI for Current User Events with MockMvc.
     */
    @Test
    @DisplayName("All Events: UnAuthorized - WithNoUser")
    void testCurrentUsersEventsPage() {

        EntityExchangeResult result = client.get().uri("/events/my")
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectHeader().location("/error/403")
                .expectBody().returnResult()
                ;
    }


    //-----------------------------------------------------------------------//
    // Events Details
    //-----------------------------------------------------------------------//

    /**
     * Test the URI for showing Event details with MockMvc.
     */
    @Test
    @DisplayName("Show Event Details - user1")
    @WithMockUser("user1@baselogic.com")
    @WithMockEventUserDetailsUser1
    void testShowEvent_user1() {

        EntityExchangeResult result = client.get().uri("/events/my")
                .exchange()
                .expectStatus().isOk()
                .expectBody().returnResult()
                ;

        String body = new String(result.getResponseBodyContent(), StandardCharsets.UTF_8);

        assertThat(body)
                    .contains("<a href=\"/events/100\">Birthday Party</a>")
//                    .contains("<a href=\"/events/101\">Mountain Bike Race</a>")
//                    .contains("<a href=\"/events/102\">Lunch</a>")
            ;
    }


    /**
     * Test the URI for showing Event details with MockMvc.
     */
    @Test
    @DisplayName("Show single Details - user1")
    @WithMockUser("user1@baselogic.com")
    @WithMockEventUserDetailsUser1
    void test_Show_single_Event_user1() {

        EntityExchangeResult result = client.get().uri("/events/100")
                .exchange()
                .expectStatus().isOk()
                .expectBody().returnResult()
                ;

        String body = new String(result.getResponseBodyContent(), StandardCharsets.UTF_8);

        assertThat(body)
                    .contains("Event Details")
                    .contains("Summary")
                    .contains("Birthday Party")
            ;
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
    void showEventForm__WithUser() {

        EntityExchangeResult result = client.get().uri("/events/form")
                .exchange()
                .expectStatus().isOk()
                .expectBody().returnResult()
                ;

        String body = new String(result.getResponseBodyContent(), StandardCharsets.UTF_8);

        assertThat(body)
                .contains("Create New Event")
        ;
    }

    //@Test
    @DisplayName("Show Event Form Auto Populate")
    @WithMockEventUserDetailsUser1
    void test_showEventFormAutoPopulate() {

        EntityExchangeResult result = client.post().uri("/events/new")
                .body(
                        BodyInserters.fromFormData("auto", "auto")
                )
                .header(HttpHeaders.CONTENT_TYPE, MediaType.MULTIPART_FORM_DATA_VALUE)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .returnResult()
                ;

        String body = new String(result.getResponseBodyContent(), StandardCharsets.UTF_8);

        assertThat(body)
                .contains("Create New Event")
                .contains("A new event....")
                .contains("This was auto-populated to save time creating a valid event.")
        ;
    }


    @Test
    @DisplayName("Submit Event Form")
    @WithMockEventUserDetailsUser1
    void test_createEvent() {

        EntityExchangeResult result = client.post().uri("/events/new")
                .body(
                        BodyInserters
                                .fromFormData("submit", "create")
                                .with("attendeeEmail", "user2@baselogic.com")
                                .with("when", "2020-07-03 00:00:01")
                                .with("summary", "Test Summary")
                                .with("description", "Test Description")
                )
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .returnResult();

        String body = new String(result.getResponseBodyContent(), StandardCharsets.UTF_8);

        assertThat(body)
                .contains("Current User Events")
                .contains("My Event List")
                .contains("Test Summary")
        ;
    }


    @Test
    @DisplayName("Submit Event Form - null email")
    @WithMockEventUserDetailsUser1
    void test_createEvent_null_email() {

        EntityExchangeResult result = client.post().uri("/events/new")
                .body(
                        BodyInserters
                                .fromFormData("submit", "create")
//                                .with("attendeeEmail", "user2@baselogic.com")
                                .with("when", "2020-07-03 00:00:01")
                                .with("summary", "Test Summary")
                                .with("description", "Test Description")
                )
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .returnResult()
                ;

        String body = new String(result.getResponseBodyContent(), StandardCharsets.UTF_8);

        assertThat(body)
                .contains("Attendee Email is required")
        ;

    }

    @Test
    @DisplayName("Submit Event Form - not found email")
    @WithMockEventUserDetailsUser1
    void test_createEvent_not_found_email() {


        EntityExchangeResult result = client.post().uri("/events/new")
                .body(
                        BodyInserters
                                .fromFormData("submit", "create")
//                                .with("attendeeEmail", "user2@baselogic.com")
                                .with("when", "2020-07-03 00:00:01")
                                .with("summary", "Test Summary")
                                .with("description", "Test Description")
                )
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .returnResult()
                ;

        String body = new String(result.getResponseBodyContent(), StandardCharsets.UTF_8);

        assertThat(body)
                .contains("Attendee Email is required")
        ;

    }


    //-----------------------------------------------------------------------//

    @Test
    @DisplayName("Submit Event Form - null when")
    @WithMockEventUserDetailsUser1
    void test_createEvent_null_when() {


        EntityExchangeResult result = client.post().uri("/events/new")
                .body(
                        BodyInserters
                                .fromFormData("submit", "create")
                                .with("attendeeEmail", "user2@baselogic.com")
//                                .with("when", "2020-07-03 00:00:01")
                                .with("summary", "Test Summary")
                                .with("description", "Test Description")
                )
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .returnResult()
                ;

        String body = new String(result.getResponseBodyContent(), StandardCharsets.UTF_8);

        assertThat(body)
                .contains("Event Date/Time is required")
        ;


    }


    //-----------------------------------------------------------------------//

    @Test
    @DisplayName("Submit Event Form - null summary")
    @WithMockEventUserDetailsUser1
    void test_createEvent_null_summary() {


        EntityExchangeResult result = client.post().uri("/events/new")
                .body(
                        BodyInserters
                                .fromFormData("submit", "create")
                                .with("attendeeEmail", "user2@baselogic.com")
                                .with("when", "2020-07-03 00:00:01")
//                                .with("summary", "Test Summary")
                                .with("description", "Test Description")
                )
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .returnResult()
                ;

        String body = new String(result.getResponseBodyContent(), StandardCharsets.UTF_8);

        assertThat(body)
                .contains("Summary is required")
        ;


    }


    //-----------------------------------------------------------------------//

    @Test
    @DisplayName("Submit Event Form - null description")
    @WithMockEventUserDetailsUser1
    void test_createEvent_null_description() {


        EntityExchangeResult result = client.post().uri("/events/new")
                .body(
                        BodyInserters
                                .fromFormData("submit", "create")
                                .with("attendeeEmail", "user2@baselogic.com")
                                .with("when", "2020-07-03 00:00:01")
                                .with("summary", "Test Summary")
//                                .with("description", "Test Description")
                )
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .returnResult()
                ;

        String body = new String(result.getResponseBodyContent(), StandardCharsets.UTF_8);

        assertThat(body)
                .contains("Description is required")
        ;


    }

    //-----------------------------------------------------------------------//

    @Test
    @DisplayName("Validate EventUserDetails")
    void test_validate_EventUserDetails() {

        assertThat(TestUtils.user1UserDetails.isAccountNonExpired()).isTrue();
        assertThat(TestUtils.user1UserDetails.isAccountNonLocked()).isTrue();
        assertThat(TestUtils.user1UserDetails.isCredentialsNonExpired()).isTrue();
        assertThat(TestUtils.user1UserDetails.isEnabled()).isTrue();

    }

} // The End...
