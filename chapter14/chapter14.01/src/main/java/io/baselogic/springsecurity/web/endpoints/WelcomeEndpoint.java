package io.baselogic.springsecurity.web.endpoints;

import io.baselogic.springsecurity.web.model.CommandDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * This displays the welcome command that shows what will be happening in each chapter.
 *
 * @author mickknutson
 * @since chapter14.01 Created from WelcomeController
 *
 */
@RestController
@Slf4j
public class WelcomeEndpoint {

    @GetMapping("/")
    public Mono<CommandDto> welcome() {

        CommandDto response = CommandDto.builder()
                .message("Welcome to the EventManager!")
                .summary("This is the 'Reactive Spring Security' Chapter")
                .description("Each chapter will have a slightly different summary depending on what has been done.")
                .build();

        return Mono.just(response);
    }

} // The End...
