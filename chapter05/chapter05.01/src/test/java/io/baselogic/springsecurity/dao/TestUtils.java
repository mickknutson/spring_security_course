package io.baselogic.springsecurity.dao;

import io.baselogic.springsecurity.domain.AppUser;
import io.baselogic.springsecurity.domain.Event;
import io.baselogic.springsecurity.domain.EventUserDetails;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

/**
 * Test Utilities
 * @author mickknutson
 *
 * @since chapter01.00
 * @since chapter03.04 added EventUserDetails
 */
public interface TestUtils {

    public static final AppUser TEST_APP_USER_1 = new AppUser(){{
        setId(42);
        setEmail("test@example.com");
        setPassword("test");
    }};

    public static final AppUser attendee = new AppUser(){{
        setId(0);
        setEmail("user1@example.com");
        setPassword("user1");
    }};
    public static final AppUser owner = new AppUser(){{
        setId(1);
        setEmail("admin1@example.com");
        setPassword("admin1");
    }};
    public static final AppUser APP_USER_1 = new AppUser(){{
        setId(1);
        setEmail("user1@example.com");
        setPassword("user1");
    }};
    public static final AppUser admin1 = new AppUser(){{
        setId(1);
        setEmail("admin1@example.com");
        setPassword("admin1");
    }};

    public static final EventUserDetails user1UserDetails = new EventUserDetails(APP_USER_1);
    public static final EventUserDetails admin1UserDetails = new EventUserDetails(admin1);


    public static final Event testEvent = new Event(){{
        setId(42);
        setAttendee(attendee);
        setOwner(owner);
    }};

    public static final Event testEvent2 = new Event(){{
        setId(24);
        setAttendee(attendee);
        setOwner(owner);
    }};

    List<Event> TEST_EVENTS = Arrays.asList(testEvent, testEvent2);

    List<AppUser> TEST_APP_USERS = Arrays.asList(TEST_APP_USER_1, attendee, owner);


    static Event createMockEvent(AppUser owner,
                                 AppUser attendee,
                                 String summary
    ) {
        Event event = new Event();
        event.setSummary(summary);
        event.setDescription("testing + " + summary);
        event.setWhen(Calendar.getInstance());
        event.setAttendee(attendee);
        event.setOwner(owner);

        return event;

    }

    static AppUser createMockUser(String email,
                                  String firstName,
                                  String lastName
    ) {
        AppUser appUser = new AppUser();
        appUser.setEmail(email);
        appUser.setPassword("*****");
        appUser.setFirstName(firstName);
        appUser.setLastName(lastName);
        return appUser;
    }

} // The End...
