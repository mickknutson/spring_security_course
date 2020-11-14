package io.baselogic.springsecurity.web.controllers;

import io.baselogic.springsecurity.domain.EventUserDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticatedPrincipal;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import reactor.core.publisher.Mono;

/**
 * This displays the welcome screen that shows what will be happening in each chapter.
 *
 * @author mickknutson
 * @since chapter01.00 Created
*
 */
@Controller
@Slf4j
public class WelcomeController {

    @GetMapping("/")
    public String welcome() {
        log.debug("* welcome");
        return "index";
    }


    @GetMapping("/login")
    public String login() {
        log.debug("* login");
        return "login";
    }


} // The End...