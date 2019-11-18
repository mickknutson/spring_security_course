package io.baselogic.springsecurity.web.controllers;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import io.baselogic.springsecurity.domain.User;
import io.baselogic.springsecurity.service.EventService;
import io.baselogic.springsecurity.service.UserContext;
import io.baselogic.springsecurity.web.model.RegistrationDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * User Registration Controller
 * TODO: If we dont use this controller, remove it from chapters that dont need it.
 */
@Controller
@RequestMapping("registration")
public class RegistrationController {

    private final UserContext userContext;
    private final EventService eventService;

    @Autowired
    public RegistrationController(final @NotNull UserContext userContext,
                                  final @NotNull EventService eventService) {
        this.userContext = userContext;
        this.eventService = eventService;
    }

    @GetMapping("/form")
    public String registration(final @ModelAttribute RegistrationDto registrationDto) {
        return "registration/form";
    }

    @PostMapping("/new")
    public String registration(final @Valid RegistrationDto registrationDto,
                         final BindingResult result,
                         final RedirectAttributes redirectAttributes) {
        if(result.hasErrors()) {
            return "registration/form";
        }

        String email = registrationDto.getEmail();
        if(eventService.findUserByEmail(email) != null) {
            result.rejectValue("email", "errors.registration.email",
                    "Email address is already in use.");
            return "registration/form";
        }

        User user = new User();
        user.setEmail(email);
        user.setFirstName(registrationDto.getFirstName());
        user.setLastName(registrationDto.getLastName());
        user.setPassword(registrationDto.getPassword());

        redirectAttributes.addFlashAttribute("message",
                "TODO we will implement registration later in the chapter");
        return "redirect:/";
    }

} // The End...
