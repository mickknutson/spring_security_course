package io.baselogic.springsecurity.domain;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Calendar;

/**
 * An {@link Event} is an item that contains an owner (the person who created it), an attendee
 * (someone who was invited to the event), when the event will occur, a summary, and a description. For simplicity, all
 * fields are required.
 *
 * @author mickknutson
 *
 */
@Data
@Builder
public class Event implements Serializable {

    private Integer id;

    @NotEmpty(message = "Summary is required")
    private String summary;

    @NotEmpty(message = "Description is required")
    private String description;

    @NotNull(message = "When is required (yyyy-MM-dd HH:mm)")
    private Calendar when;

    @NotNull(message = "Owner is required")
    private AppUser owner;

    @NotNull(message = "Attendee is required")
    private AppUser attendee;

} // The End...
