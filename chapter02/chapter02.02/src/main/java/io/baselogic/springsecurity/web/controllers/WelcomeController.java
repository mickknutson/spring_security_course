package io.baselogic.springsecurity.web.controllers;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Locale;

/**
 * This displays the welcome screen that shows what will be happening in each chapter.
 *
 * @author mickknutson
 *
 */
@Controller
@Slf4j
public class WelcomeController {

    @GetMapping(value="/")
    public String welcome() {
        return "index";
    }

} // The End...