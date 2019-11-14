package io.baselogic.springsecurity.web.controllers;

import io.baselogic.springsecurity.domain.Event;
import io.baselogic.springsecurity.domain.User;
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

@Controller
@RequestMapping("/events")
@Slf4j
public class EventsController {

    @Autowired
    private MessageSource messageSource;

    private final EventService eventService;
    private final UserContext userContext;

    @Autowired
    public EventsController(EventService eventService, UserContext userContext) {
        this.eventService = eventService;
        this.userContext = userContext;
    }

    @GetMapping("/")
    public ModelAndView allEvents() {
        return new ModelAndView("events/list", "events", eventService.findAllEvents());
    }

    @GetMapping("/my")
    public ModelAndView userEvents() {
        User currentUser = userContext.getCurrentUser();
        Integer currentUserId = currentUser.getId();
        ModelAndView result = new ModelAndView("events/my", "events", eventService.findEventByUser(currentUserId));
        result.addObject("currentUser", currentUser);
        return result;
    }

    @GetMapping("/{eventId}")
    public ModelAndView showEvent(@PathVariable Integer eventId) {
        Event event = eventService.findEventById(eventId);
        return new ModelAndView("events/show", "event", event);
    }

    @GetMapping("/form")
    public String showEventForm(@ModelAttribute EventDto eventDto) {
        return "events/create";
    }

    /**
     * Populates the form for creating an event with valid information.
     * Useful so that users do not have to think when
     * filling out the form for testing.
     *
     * @param eventDto
     */
    @PostMapping(value = "/new", params = "auto")
    public String showEventFormAutoPopulate(@ModelAttribute EventDto eventDto) {
        // provide default values to make user submission easier
        eventDto.setSummary("A new event....");
        eventDto.setDescription("This was auto-populated to save time creating a valid event.");
        eventDto.setWhen(Calendar.getInstance());

        // make the attendee not the current user
        User currentUser = userContext.getCurrentUser();

        Integer attendeeId = currentUser.getId() == 0 ? 1 : 0;

        User attendee = eventService.findUserById(attendeeId);
        eventDto.setAttendeeEmail(attendee.getEmail());

        return "events/create";
    }

    @PostMapping(value = "/new")
    public String createEvent(@Valid EventDto eventDto, BindingResult result,
                              RedirectAttributes redirectAttributes) {

        log.info("****** createEvent: {} *****", eventDto);

        if (result.hasErrors()) {
            result.getAllErrors().forEach( e ->{
                log.info("error: {}", e);
            });
            return "events/create";
        }

        User attendee = eventService.findUserByEmail(eventDto.getAttendeeEmail());
        if (attendee == null) {
            result.rejectValue("attendeeEmail", "event.new.attendeeEmail.missing");
        }

        if (result.hasErrors()) {
            return "events/create";
        }

        Event event = new Event();
        event.setAttendee(attendee);
        event.setDescription(eventDto.getDescription());
        event.setOwner(userContext.getCurrentUser());
        event.setSummary(eventDto.getSummary());
        event.setWhen(eventDto.getWhen());
        eventService.createEvent(event);

        String success = messageSource.getMessage("event.new.success",
                null, LocaleContextHolder.getLocale());

        redirectAttributes.addFlashAttribute("message", success);

        return "redirect:/events/my";
    }

} // The End...
