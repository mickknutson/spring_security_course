package io.baselogic.springsecurity.web.model;

import io.baselogic.springsecurity.domain.Event;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Calendar;

/**
 * A form object that is used for creating a new Command Message.
 *
 * @author mickknutson
 *
 * @since chapter14.01 Created
 *
 */
// Lombok Annotations:
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommandDto {

    @NotEmpty(message = "Message is required")
    private String message;
    @NotEmpty(message = "Summary is required")
    private String summary;
    @NotEmpty(message = "Description is required")
    private String description;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    @NotNull(message = "Event Date/Time is required")
    private LocalDateTime timestamp;

} // The End...