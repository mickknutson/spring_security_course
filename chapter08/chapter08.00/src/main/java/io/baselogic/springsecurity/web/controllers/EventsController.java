package io.baselogic.springsecurity.web.controllers;

import io.baselogic.springsecurity.domain.AppUser;
import io.baselogic.springsecurity.domain.Event;
import io.baselogic.springsecurity.service.EventService;
import io.baselogic.springsecurity.service.UserContext;
import io.baselogic.springsecurity.web.model.EventDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.Calendar;

/**
 * Event Controller
 *
 * @since chapter01.00
 */
@Controller
@RequestMapping("/events")
//@Validated
@Slf4j
public class EventsController {

    private final MessageSource messageSource;

    private final EventService eventService;
    private final UserContext userContext;

    private static final String EVENT_CREATE_VIEW = "events/create";
    private static final String EVENT_LIST_VIEW = "events/list";
    private static final String EVENT_MY_VIEW = "events/my";
    private static final String EVENT_SHOW_VIEW = "events/show";

    @Autowired
    public EventsController(final EventService eventService,
                            final UserContext userContext,
                            final MessageSource messageSource) {
        this.eventService = eventService;
        this.userContext = userContext;
        this.messageSource = messageSource;
    }

    @GetMapping("/")
    public ModelAndView allEvents() {
        return new ModelAndView(EVENT_LIST_VIEW, "events", eventService.findAllEvents());
    }

    @GetMapping("/my")
    public ModelAndView userEvents() {
        AppUser currentAppUser = userContext.getCurrentUser();
        Integer currentUserId = currentAppUser.getId();

        ModelAndView result = new ModelAndView(EVENT_MY_VIEW, "events", eventService.findEventByUser(currentUserId));
        result.addObject("currentAppUser", currentAppUser);
        return result;
    }

    @GetMapping("/{eventId}")
    public ModelAndView showEvent(final @PathVariable Integer eventId) {
        Event event = eventService.findEventById(eventId);
        return new ModelAndView(EVENT_SHOW_VIEW, "event", event);
    }

    @GetMapping("/form")
    public String showEventForm(final @ModelAttribute EventDto eventDto) {
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
     *
     */
    @PostMapping(value = "/new", params = "auto")
    public String showEventFormAutoPopulate(final @ModelAttribute EventDto eventDto) {
        // provide default values to make user submission easier
        eventDto.setSummary("A new event....");
        eventDto.setDescription("This was auto-populated to save time creating a valid event.");
        eventDto.setWhen(Calendar.getInstance());

        // make the attendee not the current user
        AppUser currentAppUser = userContext.getCurrentUser();

        // @since chapter03.04 removed user id check
        Integer attendeeId = currentAppUser.getId();

        AppUser attendee = eventService.findUserById(attendeeId);
        eventDto.setAttendeeEmail(attendee.getEmail());

        return EVENT_CREATE_VIEW;
    }

    @PostMapping("/new")
    public String createEvent(final @Valid EventDto eventDto,
                              final BindingResult result,
                              final RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            result.getAllErrors().forEach( e -> log.info("error: {}", e) );
            return EVENT_CREATE_VIEW;
        }

        AppUser attendee = eventService.findUserByEmail(eventDto.getAttendeeEmail());
        if (attendee == null) {
            result.rejectValue("attendeeEmail", "event.new.attendeeEmail.missing");
        }

        if (result.hasErrors()) {
            return EVENT_CREATE_VIEW;
        }

        Event event = new Event();
        event.setSummary(eventDto.getSummary());
        event.setDescription(eventDto.getDescription());
        event.setWhen(eventDto.getWhen());
        event.setAttendee(attendee);
        event.setOwner(userContext.getCurrentUser());

        eventService.createEvent(event);

        String success = messageSource.getMessage("event.new.success",
                null, LocaleContextHolder.getLocale());

        redirectAttributes.addFlashAttribute("message", success);

        return "redirect:/events/my";
    }

} // The End...
