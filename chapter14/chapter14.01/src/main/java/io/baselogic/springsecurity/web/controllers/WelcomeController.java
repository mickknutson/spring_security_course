package io.baselogic.springsecurity.web.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

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