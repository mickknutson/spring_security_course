package io.baselogic.springsecurity.web.controllers;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@Slf4j
public class LoginController {

    @GetMapping("/login")
    public ModelAndView login(
            @RequestParam(value = "error", required = false) String error,
            @RequestParam(value = "logout", required = false) String logout) {

        log.info("******login(error): {} ***************************************", error);
        log.info("******login(logout): {} ***************************************", logout);

        ModelAndView model = new ModelAndView();
        if (error != null) {
            model.addObject("error", "Invalid username and password!");
        }

        if (logout != null) {
            model.addObject("message", "You've been logged out successfully.");
        }
        model.setViewName("login");

        return model;

    }

    // Login form with error
//    @GetMapping("/login_error")
    public String loginError(Model model) {
        model.addAttribute("error", true);
        log.info("******loginError() contains error? : {} ******************************************",
                model.containsAttribute("error"));
        return "login";
    }

    // Login form with error
//    @GetMapping("/logout")
    public String logout(Model model) {
        model.addAttribute("error", true);
        log.info("******logout() contains error? : {} ******************************************",
                model.containsAttribute("error"));
        return "login";
    }
}
