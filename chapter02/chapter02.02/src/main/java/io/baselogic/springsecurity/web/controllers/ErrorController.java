package io.baselogic.springsecurity.web.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
@Slf4j
public class ErrorController {

    @ExceptionHandler(Throwable.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ModelAndView handleInternalServerError(final Throwable throwable) {

        String errorMessage = "Unknown error";

        if(throwable != null){
            log.error("Exception during execution of SpringSecurity application: {}", throwable.getMessage(), throwable);
            errorMessage = throwable.getMessage();
        }

        return new ModelAndView("error", "error", errorMessage);
    }

} // The End...
