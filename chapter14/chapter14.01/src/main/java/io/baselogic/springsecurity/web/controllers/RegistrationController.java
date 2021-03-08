package io.baselogic.springsecurity.web.controllers;

import io.baselogic.springsecurity.domain.AppUser;
import io.baselogic.springsecurity.domain.Event;
import io.baselogic.springsecurity.service.EventService;
import io.baselogic.springsecurity.service.UserContext;
import io.baselogic.springsecurity.web.model.RegistrationDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import reactor.core.publisher.Mono;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * User Registration Controller
 *
 * @author mickknutson
 *
 * @since chapter03.01 Does not create a User in the database.
 * @since chapter03.02 Creates a new User object with the eventService and sets it in the userContext
 * @since chapter04.01 The registration form does not add user to USERS table, only APPUSERS
 * @since chapter14.01 Updated for WebFlux
 */
@Controller
@RequestMapping("/registration")
@Slf4j
public class RegistrationController {


    private final EventService eventService;
    private final UserContext userContext;

    private static final String REG_FORM_VIEW = "registration/register";


    public RegistrationController(final EventService eventService,
                                  final UserContext userContext) {
        Assert.notNull(eventService, "eventService cannot be null");
        Assert.notNull(userContext, "userContext cannot be null");
        this.userContext = userContext;
        this.eventService = eventService;
    }

    @GetMapping("/form")
    public String showRegistrationForm(final @ModelAttribute RegistrationDto registrationDto) {
        return REG_FORM_VIEW;
    }

    @PostMapping("/new")
    public Mono<String> createRegistration(final Model model,
                                           final @Valid RegistrationDto registrationDto,
                                           final BindingResult result) {

        log.info("* createRegistration");
        if (result.hasErrors()) {
            log.error("* result.hasErrors()");
            result.getAllErrors().forEach( e -> log.error("error: {}", e) );
            return Mono.just(REG_FORM_VIEW);
        }

        //-----------------------------------------------------------------------//
        AppUser newUser = AppUser.builder()
                .firstName(registrationDto.getFirstName())
                .lastName(registrationDto.getLastName())
                .email(registrationDto.getEmail())
                .password(registrationDto.getPassword())
                .build();

        //-----------------------------------------------------------------------//
        Mono<AppUser> newUserMono =
                eventService.findUserByEmail(newUser.getEmail())
                        .doOnSuccess(existingUser -> {
                            log.debug("verifying if user exists: {}.", newUser.getEmail());
                            if (existingUser != null) {
                                log.error("Email address is already in use: {}.", existingUser.getEmail());
                                result.rejectValue("email", "errors.registration.email",
                                        "Email address is already in use.");
                                throw new IllegalArgumentException("Email address is already in use: " + newUser.getEmail());
                            }
                        })
                        .switchIfEmpty(Mono.defer(() -> {
                            log.info("Email address not already in use!");
                            return Mono.just(newUser);
                        }))
                ;
//        newUserMono.subscribe();

        //-----------------------------------------------------------------------//
        // return New Error?
//        if (result.hasErrors()) {
//            log.error("hasErrors !");
//            result.getAllErrors().forEach(e -> log.error("** error: {}", e) );
//            return Mono.just(REG_FORM_VIEW);
//        }

        //-----------------------------------------------------------------------//

//        Mono<AppUser> newAppUserMono2 = registrationMono
//                .flatMap(eventService::createUser)
//                .doOnSuccess(newRegistration -> {
//                    log.debug("Newly Created user {}.", newRegistration);
//                }).log();

        Mono<AppUser> createdAppUserMono = newUserMono
                .flatMap(eventService::createUser);

        Mono<AppUser> authenticatedUserMono = createdAppUserMono
                .map(u -> {
                    log.debug("u {}.", u);
                    userContext.setCurrentUser(u).subscribe();
                    return u;
                })
                .map(y -> {
                    log.debug("* y {}.", y);
                    userContext.getCurrentUser()
                            .doOnEach(z -> log.debug("* z {}.", z));
                    return y;
                })
                ;


        /*Mono<AppUser> authenticatedUserMono = createdAppUserMono.zipWhen(createdAppUser ->

                        userContext.setCurrentUser(createdAppUser),

                (createdAppUser, authenticatedUser) -> {
                    log.debug("* createdAppUser {}, authenticatedUser: {}", createdAppUser, authenticatedUser);
                    return createdAppUser;
                });*/

        authenticatedUserMono
                .doOnSuccess(u -> log.debug("u {}.", u)).subscribe();



        // Do this for testing
//        Mono<AppUser> currentUserMono = userContext.getCurrentUser();
//        currentUserMono.doOnSuccess(u -> log.debug("u {}.", u)).subscribe();

        /*authenticatedUserMono
                .doOnSuccess(u -> {
                    StringBuilder sb = new StringBuilder("Registration Successful.");
                    sb.append(" Account created for '")
                            .append(newUser.getEmail())
                            .append("' and automatically logged-in.");
                    log.info(sb.toString());
                    model.addAttribute("message", sb.toString());

//            return Mono.just("redirect:/");

        });*/

        return Mono.just("redirect:/");

    }

} // The End...
