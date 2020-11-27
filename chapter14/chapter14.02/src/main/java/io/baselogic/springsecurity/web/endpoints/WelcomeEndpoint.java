package io.baselogic.springsecurity.web.endpoints;

import io.baselogic.springsecurity.web.model.CommandDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * This displays the welcome screen that shows what will be happening in each chapter.
 *
 * @author mickknutson
 * @since chapter01.00 Created
*
 */
@RestController
@Slf4j
public class WelcomeEndpoint {

    @GetMapping("/")
    public CommandDto welcome() {

        return CommandDto.builder()
                .message("Welcome to the EventManager!")
                .summary("Chapter14.01")
                .description("Welcome to the EventManager!")
                .build();
    }

} // The End...