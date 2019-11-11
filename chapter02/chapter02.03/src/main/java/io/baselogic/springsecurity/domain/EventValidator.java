package io.baselogic.springsecurity.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.*;

@Component
public class EventValidator implements Validator {

    private static final Logger log = LoggerFactory.getLogger(EventValidator.class);

    @Override
    public boolean supports(Class<?> clazz) {
        return Event.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        if (errors.getErrorCount() == 0) {

            Event event = (Event) target;

            if (event == null) {
                errors.reject("100",
                        "event cannot be null");
            }

            if (event.getId() != null) {
                errors.reject("101",
                        "event.getId() must be null when creating a new Event");
            }
            final User owner = event.getOwner();
            if (owner == null) {
                errors.reject("102",
                        "event.getOwner() cannot be null");
            }
            final User attendee = event.getAttendee();
            if (attendee == null) {
                errors.reject("103",
                        "attendee.getOwner() cannot be null");
            }
            final Calendar when = event.getWhen();
            if (when == null) {
                errors.reject("104",
                        "event.getWhen() cannot be null");
            }
        }
    }

} // The End...