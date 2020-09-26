package io.baselogic.springsecurity.web.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * This displays the welcome screen that shows what will be happening in each chapter.
 *
 * @since chapter01.00
 * @author mickknutson
 *
 */
@Controller
@Slf4j
public class WelcomeController {

    @GetMapping("/")
    public String welcome() {
        return "index";
    }

} // The End...