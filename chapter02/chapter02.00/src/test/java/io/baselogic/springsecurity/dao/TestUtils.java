package io.baselogic.springsecurity.dao;

import io.baselogic.springsecurity.domain.AppUser;
import io.baselogic.springsecurity.domain.Event;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public interface TestUtils {

    public static final AppUser TEST_APP_USER_1 = new AppUser(){{
        setId(42);
        setEmail("test@baselogic.com");
    }};

    public static final AppUser testUser1 = new AppUser(){{
        setId(42);
        setEmail("test@baselogic.com");
        setPassword("test");
    }};

    public static final AppUser attendee = new AppUser(){{
        setId(0);
        setEmail("user1@baselogic.com");
        setPassword("user1");
    }};
    public static final AppUser owner = new AppUser(){{
        setId(1);
        setEmail("admin1@baselogic.com");
        setPassword("admin1");
    }};
    public static final AppUser user1 = new AppUser(){{
        setId(1);
        setEmail("user1@baselogic.com");
        setPassword("user1");
    }};
    public static final AppUser admin1 = new AppUser(){{
        setId(1);
        setEmail("admin1@baselogic.com");
        setPassword("admin1");
    }};


    public static final Event testEvent = Event.builder()
            .id(42)
            .attendee(attendee)
            .owner(owner)
            .build();


    public static final Event testEvent2 = Event.builder()
            .id(24)
            .attendee(attendee)
            .owner(owner)
            .build();

    List<Event> TEST_EVENTS = Arrays.asList(testEvent, testEvent2);

    List<AppUser> TEST_USERS = Arrays.asList(testUser1, attendee, owner);

    List<AppUser> TEST_APP_USERS = Arrays.asList(TEST_APP_USER_1, attendee, owner);


    static Event createMockEvent(AppUser owner,
                                 AppUser attendee,
                                 String summary
    ) {
        return Event.builder()
                .summary(summary)
                .description("testing + " + summary)
                .when(Calendar.getInstance())
                .attendee(attendee)
                .owner(owner)
                .build();

    }

    static AppUser createMockUser(String email,
                                  String firstName,
                                  String lastName
    ) {
        AppUser user = new AppUser();
        user.setEmail(email);
        user.setPassword("*****");
        user.setFirstName(firstName);
        user.setLastName(lastName);
        return user;
    }

} // The End...
