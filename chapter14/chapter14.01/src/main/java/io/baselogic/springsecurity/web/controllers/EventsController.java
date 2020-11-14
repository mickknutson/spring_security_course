package io.baselogic.springsecurity.web.controllers;

import io.baselogic.springsecurity.domain.AppUser;
import io.baselogic.springsecurity.domain.Event;
import io.baselogic.springsecurity.service.EventService;
import io.baselogic.springsecurity.service.UserContext;
import io.baselogic.springsecurity.web.model.EventDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
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
//@Validated
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
        log.debug("* allEvents");

        Flux<Event> events = eventService.findAllEvents();
        model.addAttribute("events", events);

        return EVENT_LIST_VIEW;
    }

    @GetMapping("/my")
    public Mono<String> userEvents(final Model model) {
        log.debug("* userEvents");

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
    public Mono<String> showEventFormAutoPopulate(final Model model,
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

    @PostMapping("/new")
    public Mono<String> createEvent(final Model model,
                                    final @Valid EventDto eventDto,
                                    final BindingResult result) {

        log.debug("* createEvent({}, {})", model, eventDto);

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

        // TODO: Might consider moving this to the Service layer:
        Mono<AppUser> attendeeMono = eventService.findUserByEmail(eventDto.getAttendeeEmail());

        Mono<Event> newEventMono = eventInputMono.zipWith(
                attendeeMono, (event, attendee) -> {
                    if (attendee == null) {
                        result.rejectValue("attendeeEmail", "event.new.attendeeEmail.missing");
                    } else {
                        log.debug("attendee.subscribe: {}", attendee);
                        event.setAttendee(attendee);
                    }
                    return event;
                });


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
                    log.debug("newEventMono.zipWith: e: {}, o: {}", event, owner);
                    return event;
                });


        return finalEventMono.flatMap(eventService::createEvent)
                .then(userEvents(model));

    }

} // The End...
