package io.baselogic.springsecurity.web.controllers;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
@Slf4j
public class ErrorController {

    @ExceptionHandler(Throwable.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ModelAndView exception_INTERNAL_SERVER_ERROR(final Throwable throwable, final Model model) {

        log.error("Exception during execution of SpringSecurity application: {}", throwable.getMessage(), throwable);
        String errorMessage = (throwable != null ? throwable.getMessage() : "Unknown error");

        return new ModelAndView("error", "error", errorMessage);
    }

} // The End...
