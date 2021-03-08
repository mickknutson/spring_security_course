package io.baselogic.springsecurity.web.controllers;

import io.baselogic.springsecurity.domain.AppUser;
import io.baselogic.springsecurity.domain.Event;
import io.baselogic.springsecurity.service.EventService;
import io.baselogic.springsecurity.service.UserContext;
import io.baselogic.springsecurity.web.model.EventDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Calendar;

/**
 * Event Controller
 *
 * @author mickknutson
 * @since chapter01.00 Created
 * @since chapter14.01 Updated for Reactive WebFlux
 */
@Controller
@RequestMapping("/events")
@Slf4j
public class EventsController {

    private final EventService eventService;
    private final UserContext userContext;
    private final MessageSource messageSource;

    private static final String EVENT_CREATE_VIEW = "events/create";
    private static final String EVENT_LIST_VIEW = "events/list";
    private static final String EVENT_MY_VIEW = "events/my";
    private static final String EVENT_SHOW_VIEW = "events/show";

    public EventsController(final EventService eventService,
                            final UserContext userContext,
                            final MessageSource messageSource) {
        Assert.notNull(eventService, "eventService cannot be null");
        Assert.notNull(userContext, "userContext cannot be null");
        Assert.notNull(messageSource, "messageSource cannot be null");

        this.eventService = eventService;
        this.userContext = userContext;
        this.messageSource = messageSource;
    }

    @GetMapping("/")
    public String allEvents(final Model model) {

        Flux<Event> events = eventService.findAllEvents();
        model.addAttribute("events", events);

        return EVENT_LIST_VIEW;
    }

    @GetMapping("/my")
    public Mono<String> userEvents(final Model model) {

        return userContext.getCurrentUser()
                .map( appUser -> {
                    Flux<Event> results = eventService.findEventByUser(appUser.getId());
                    model.addAttribute("currentAppUser", appUser);
                    model.addAttribute("events", results);
                    return EVENT_MY_VIEW;

                });
    }

    @GetMapping("/{eventId}")
    public Mono<String> showEvent(final Model model, final @PathVariable Integer eventId) {
        log.debug("* showEvent({})", eventId);

        return eventService.findEventById(eventId)
                .map(event -> {
                    model.addAttribute("event", event);
                    return EVENT_SHOW_VIEW;
                });
    }

    @GetMapping("/form")
    public String showEventForm(final @ModelAttribute EventDto eventDto) {
        log.debug("* showEventForm({})", eventDto);
        return EVENT_CREATE_VIEW;
    }

    /**
     * Populates the form for creating an event with valid information.
     * Useful so that users do not have to think when
     * filling out the form for testing.
     *
     * @param eventDto Event data transfer Object
     *
     * @since chapter03.04 removed user id check (currentAppUser.getId() == 0 ? 1 : 0;)
     * @since chapter14.01 Updated for WebFlux
     *
     */
    @PostMapping(value = "/new", params = "auto")
    public Mono<String> autoPopulateShowEventForm(final Model model,
                                                  final @ModelAttribute EventDto eventDto) {
        log.debug("*** showEventFormAutoPopulate({}, {})", model, eventDto);

        // provide default values to make user submission easier
        eventDto.setSummary("A new event....");
        eventDto.setDescription("This was auto-populated to save time creating a valid event.");
        eventDto.setWhen(Calendar.getInstance());

        Mono<AppUser> currentUserMono = userContext.getCurrentUser();

        Mono<AppUser> attendeeMono = currentUserMono.flatMap(currentUser -> {
                return eventService.findUserById(currentUser.getId());
        });

        return attendeeMono.doOnSuccess(attendee ->{
            eventDto.setAttendeeEmail(attendee.getEmail());
            model.addAttribute("eventDto", eventDto);

        }).then(Mono.just(EVENT_CREATE_VIEW));
    }

    @PostMapping(value = "/new")
    public Mono<String> createEvent(final Model model,
                                    final @Valid EventDto eventDto,
                                    final BindingResult result) {

        if (result.hasErrors()) {
            result.getAllErrors().forEach( e -> log.error("** error: {}", e) );
            return Mono.just(EVENT_CREATE_VIEW);
        }

        //-----------------------------------------------------------------------//

        Event newEvent = Event.builder()
                .summary(eventDto.getSummary())
                .description(eventDto.getDescription())
                .when(eventDto.getWhen())
                .build();

        Mono<Event> eventInputMono = Mono.just(newEvent);


        //-----------------------------------------------------------------------//

        Mono<AppUser> attendeeMono = eventService.findUserByEmail(eventDto.getAttendeeEmail());

        // CTRL + SPACE
        // CTRL + B

        Mono<Event> newEventMono = eventInputMono.zipWith(
                attendeeMono, (event, attendee) -> {
                    log.info("***** Event: {}, Attendee: {}", event, attendee);
                    if (attendee == null) {
                        result.rejectValue("attendeeEmail", "event.new.attendeeEmail.missing");
                    } else {
                        event.setAttendee(attendee);
                    }
                    return event;
                }).switchIfEmpty(Mono.defer(() -> {
                    log.info("Attendee Not found!");
                    result.rejectValue("attendeeEmail", "event.new.attendeeEmail.missing");
                    return Mono.just(new Event());
                }))
                .log("1.ZIPWITH");

        newEventMono.subscribe();

        //-----------------------------------------------------------------------//
        // New Error?
        if (result.hasErrors()) {
            result.getAllErrors().forEach(e -> log.error("** error: {}", e) );
            return Mono.just(EVENT_CREATE_VIEW);
        }

        Mono<AppUser> currentUserMono = userContext.getCurrentUser();

        Mono<Event> finalEventMono = newEventMono.zipWith(
                currentUserMono, (event, owner) -> {
                    event.setOwner(owner);
                    return event;
                }).log("2.ZIPWITH");

        return finalEventMono.flatMap(eventService::createEvent)
                .then(userEvents(model));

    }

} // The End...
