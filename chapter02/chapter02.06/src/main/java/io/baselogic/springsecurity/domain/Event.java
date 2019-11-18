package io.baselogic.springsecurity.domain;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Calendar;

/**
 * An {@link Event} is an item on a calendar that contains an owner (the person who created it), an attendee
 * (someone who was invited to the event), when the event will occur, a summary, and a description. For simplicity, all
 * fields are required.
 *
 * @author mickknutson
 *
 */
@Data
public class Event {

    private Integer id;

    @NotEmpty(message = "Summary is required")
    private String summary;

    @NotEmpty(message = "Description is required")
    private String description;

    @NotNull(message = "When is required (yyyy-MM-dd HH:mm)")
    private Calendar when;

    @NotNull(message = "Owner is required")
    private User owner;

    @NotNull(message = "Attendee is required")
    private User attendee;

} // The End...
