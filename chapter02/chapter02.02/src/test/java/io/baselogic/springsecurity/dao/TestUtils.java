package io.baselogic.springsecurity.dao;

import io.baselogic.springsecurity.domain.Event;
import io.baselogic.springsecurity.domain.User;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public interface TestUtils {

    public static final User testUser1 = new User(){{
        setId(42);
        setEmail("test@example.com");
    }};

    public static final User attendee = new User(){{
        setId(0);
        setEmail("user1@example.com");
    }};
    public static final User owner = new User(){{
        setId(1);
        setEmail("admin1@example.com");
    }};
    public static final User admin1 = new User(){{
        setId(1);
        setEmail("admin1@example.com");
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

    List<User> TEST_USERS = Arrays.asList(testUser1, attendee, owner);


    static Event createMockEvent(User owner,
                                 User attendee,
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

    static User createMockUser(String email,
                               String firstName,
                               String lastName
    ) {
        User user = new User();
        user.setEmail(email);
        user.setPassword("*****");
        user.setFirstName(firstName);
        user.setLastName(lastName);
        return user;
    }

} // The End...
