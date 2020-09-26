package io.baselogic.springsecurity.web.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpServletRequest;

/**
 * This displays the welcome screen that shows what will be happening in each chapter.
 *
 * @since chapter01.00
 * @author mickknutson
 *
 *  @since chapter09.02 showCreateLink() method
 */
@Controller
@Slf4j
public class WelcomeController {

    @GetMapping("/")
    public String welcome() {
        return "index";
    }

    /**
     * Populates a {@link HttpServletRequest} attribute named usernameContainsUser for any URL processed by this
     * controller. The result is based upon if the username contains "user".
     *
     * @param authentication
     *            Contains the current {@link Authentication} object. This is a more simple way of obtaining the
     *            Authentication from {@link SecurityContextHolder#getContext()}.
     * @return
     */
    @ModelAttribute("showCreateLink")
    public boolean showCreateLink(Authentication authentication) {
        log.info("*** authentication: " + authentication);
        // NOTE We could also get the Authentication from SecurityContextHolder.getContext().getAuthentication()
        boolean result = authentication != null && authentication.getName().contains("user");
        log.info("*** result: {}", result);
        return result;
    }


} // The End...