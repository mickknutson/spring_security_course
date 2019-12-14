package io.baselogic.springsecurity.web.model;

import io.baselogic.springsecurity.domain.Event;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Calendar;

/**
 * A form object that is used for creating a new {@link Event}. Using a different object is one way of preventing
 * malicious users from filling out field that they should not (i.e. fill out a different owner field).
 *
 * @author mickknutson
 *
 */
@Data
public class EventDto {

    @NotEmpty(message = "Summary is required")
    private String summary;
    @NotEmpty(message = "Description is required")
    private String description;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    @NotNull(message = "Event Date/Time is required")
    private Calendar when;

    @NotEmpty(message = "Attendee Email is required")
    @Email(message = "Attendee Email must be a valid email")
    private String attendeeEmail;

} // The End...