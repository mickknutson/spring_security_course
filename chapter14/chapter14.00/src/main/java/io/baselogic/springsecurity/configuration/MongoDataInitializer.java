package io.baselogic.springsecurity.configuration;

import io.baselogic.springsecurity.domain.AppUser;
import io.baselogic.springsecurity.domain.Event;
import io.baselogic.springsecurity.domain.Role;
import io.baselogic.springsecurity.repository.AppUserRepository;
import io.baselogic.springsecurity.repository.EventRepository;
import io.baselogic.springsecurity.repository.RoleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Initialize the initial data in the MongoDb
 * This replaces data.sql and schema.sql
 *
 * @author mickknutson
 * @since chapter05.02 Created for Mongo
 *
 */
@Configuration
@Slf4j
public class MongoDataInitializer {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private EventRepository eventRepository;

    @PostConstruct
    public void setUp() {
        log.debug(STAR);
        log.debug("* Clean the database");
        log.debug(LINE);

        appUserRepository.deleteAll();
        roleRepository.deleteAll();
        eventRepository.deleteAll();

        log.debug("seedRoles");
        seedRoles();

        log.debug("seedAppUsers");
        seedAppUsers();
        log.debug("seedEvents");
        seedEvents();

        log.debug(LINE);
        log.debug("* The End...");
        log.debug(STAR);
    }

    private AppUser user1;
    private AppUser admin1;
    private AppUser user2;


    private Role user_role;
    private Role admin_role;

    /**
     * -- ROLES --
     * insert into role(id, name) values (0, "ROLE_USER");
     * insert into role(id, name) values (1, "ROLE_ADMIN");
     */
    private void seedRoles(){
        user_role = new Role(0, "ROLE_USER");
        user_role = roleRepository.save(user_role);

        admin_role = new Role(1, "ROLE_ADMIN");
        admin_role = roleRepository.save(admin_role);
    }


    /**
     * Seed initial events
     */
    private void seedEvents(){

        // Event 1
        Event event1 = new Event(
                100,
                "Birthday Party",
                "Time to have my yearly party!",
                new GregorianCalendar(2020,6,3,6,36,00),
                user1, // Owner
                admin1 // Attendee
        );

        // Event 2
        Event event2 = new Event(
                101,
                "Mountain Bike Race",
                "Deer Valley mountain bike race",
                new GregorianCalendar(2020,11,23,13,00,00),
                user2, // Owner
                user1  // Attendee
        );

        // Event 3
        Event event3 = new Event(
                102,
                "Lunch",
                "Eating lunch together",
                new GregorianCalendar(2020,8,14,11,30,00),
                admin1, // Owner
                user2   // Attendee
        );

        // save Event
        eventRepository.save(event1);
        eventRepository.save(event2);
        eventRepository.save(event3);

        List<Event> results = eventRepository.findAll();

        log.info("*** Events [{}]:", results.size());
        results.forEach(e -> {
            log.info("Event: {} \n", e);
            log.info("summary: {}, owner: {}, attendee: {}",
                    e.getSummary(),
                    e.getOwner().getEmail(),
                    e.getAttendee().getEmail());
        });
    }


    private void seedAppUsers(){
        user1 = new AppUser(0, "user1@baselogic.com","{bcrypt}$2a$04$qr7RWyqOnWWC1nwotUW1nOe1RD5.mKJVHK16WZy6v49pymu1WDHmi","User","1");
        admin1 = new AppUser(1,"admin1@baselogic.com","{bcrypt}$2a$04$0CF/Gsquxlel3fWq5Ic/ZOGDCaXbMfXYiXsviTNMQofWRXhvJH3IK","Admin","1");
        user2 = new AppUser(2,"user2@baselogic.com","{bcrypt}$2a$04$PiVhNPAxunf0Q4IMbVeNIuH4M4ecySWHihyrclxW..PLArjLbg8CC","User2","2");

        // user1
        user1.addRole(user_role);

        // admin1
        admin1.addRole(user_role);
        admin1.addRole(admin_role);

        // user2
        user2.addRole(user_role);

        // AppUser
        appUserRepository.save(user1);
        appUserRepository.save(admin1);
        appUserRepository.save(user2);

        List<AppUser> results = appUserRepository.findAll();

        log.info("*** AppUsers [{}]:", results.size());
        results.forEach(e -> {
            log.info("AppUser: {} \n", e);
            log.info("id: {}, email: {}, passeword: {}",
                    e.getId(),
                    e.getEmail(),
                    e.getPassword());

        });
    }

    public static final String LINE = "------------------------------------------------";
    public static final String STAR = "*******************************************************";


} // The End...
