package io.baselogic.springsecurity.web.endpoints;

import io.baselogic.springsecurity.domain.AppUser;
import io.baselogic.springsecurity.service.EventService;
import io.baselogic.springsecurity.service.UserContext;
import io.baselogic.springsecurity.web.model.CommandDto;
import io.baselogic.springsecurity.web.model.RegistrationDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.util.UriBuilder;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.net.URI;

/**
 * User Registration Controller
 *
 * @author mickknutson
 *
 * @since chapter14.01 Refactored for WebFlux
 */
@RestController
@RequestMapping("registration")
//@Validated
@Slf4j
public class RegistrationEndpoint {

    private final UserContext userContext;
    private final EventService eventService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public RegistrationEndpoint(final @NotNull UserContext userContext,
                                final @NotNull EventService eventService,
                                final @NotNull PasswordEncoder passwordEncoder) {
        this.userContext = userContext;
        this.eventService = eventService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/{id}")
    public Mono<RegistrationDto> registrationDetails(final @PathVariable Integer id) {
        return Mono.just(RegistrationDto.builder().build());
    }

    @GetMapping("/form")
    public Mono<RegistrationDto> registration() {
        return Mono.just(RegistrationDto.builder().build());
    }

    @PostMapping("/new")
    public Mono<ServerResponse> registration(final @Valid @RequestBody RegistrationDto registration,
                                             final ServerRequest serverRequest) {
        /*
        UUID id = UUID.randomUUID();

        URI uri = UriComponentsBuilder.newInstance().pathSegment("v1", "stuffs", id.toString()).build().toUri();

        return stuff
            .map(it -> stuffService.create(id, it))
            .map(it -> ResponseEntity.created(uri).build());
         */


        CommandDto command;

        /*if (result.hasErrors()) {
            StringBuilder sb = new StringBuilder();
            result.getAllErrors().forEach( e -> sb.append(e) );
            log.info("error(s): {}", sb.toString());

            command = CommandDto.builder()
                    .message("Registration contains error")
                    .description(sb.toString())
                    .build();

            return Mono.just(ResponseEntity.badRequest().body(command));
        }*/

        RegistrationDto registrationDto = registration;

        String email = registrationDto.getEmail();
        if(eventService.findUserByEmail(email) != null) {
            command = CommandDto.builder()
                    .message("errors.registration.email")
                    .description("Email address is already in use.")
                    .build();

            return ServerResponse.badRequest().body(command, CommandDto.class);
        }

        AppUser appUser = new AppUser();
        appUser.setEmail(email);
        appUser.setFirstName(registrationDto.getFirstName());
        appUser.setLastName(registrationDto.getLastName());
        appUser.setPassword(
                passwordEncoder.encode(registrationDto.getPassword())
        );

        Integer id = eventService.createUser(appUser).block();
        log.info("Created user ID {}.", id);
        appUser.setId(id);

        userContext.setCurrentUser(appUser);

        StringBuilder sb = new StringBuilder("Registration Successful.");
        sb.append(" Account created for '").append(email).append("' and automatically logged-in.");

        URI uri = UriComponentsBuilder.newInstance().pathSegment("registration", id.toString()).build().toUri();

        UriBuilder uriBuilder = serverRequest.uriBuilder();
        log.info("Location {}.", uri);

        command = CommandDto.builder()
                .message("Registration Successful.")
                .description(sb.toString())
                .build();

        ResponseEntity.created(uri).build();

        return ServerResponse.created(uri).body(command, CommandDto.class);
    }

} // The End...
