package io.baselogic.springsecurity.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
// JPA Annotations:
@Entity
@Table(name = "events")

// Lombok Annotations:
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Event implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotEmpty(message = "Summary is required")
    private String summary;

    @NotEmpty(message = "Description is required")
    private String description;

    @NotNull(message = "When is required (yyyy-MM-dd HH:mm)")
    @Column(name="event_date")
    private Calendar when;


    @NotNull(message = "Owner is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="owner", referencedColumnName="id")
    private AppUser owner;

    @NotNull(message = "Attendee is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="attendee", referencedColumnName="id")
    private AppUser attendee;


    // Setter / Getter generated by Lombok

} // The End...
