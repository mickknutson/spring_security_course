package io.baselogic.springsecurity.dao;

import io.baselogic.springsecurity.domain.User;
import io.baselogic.springsecurity.domain.Event;

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


    public static final Event testEvent = new Event(){{
        setId(42);
        setOwner(owner);
        setAttendee(attendee);
    }};

    public static final Event testEvent2 = new Event(){{
        setId(24);
        setOwner(owner);
        setAttendee(attendee);
    }};

    List<Event> TEST_EVENTS = Arrays.asList(testEvent, testEvent2);

    List<User> TEST_USERS = Arrays.asList(testUser1, attendee, owner);


    static Event createMockEvent(User owner,
                                 User attendee,
                                 String summary
    ) {
        Event event = new Event();
        event.setOwner(owner);
        event.setAttendee(attendee);
        event.setSummary(summary);
        event.setDescription("testing + " + summary);
        event.setWhen(Calendar.getInstance());
        return event;
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
