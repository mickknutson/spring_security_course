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
    public ModelAndView handleInternalServerError(final Throwable e) {

        StringBuilder sb = new StringBuilder();
        sb.append("<h2>Unknown error</h2>").append("<br />");
        sb.append("Exception during execution of SpringSecurity application:").append("<br />");

        if(e != null){
            sb.append(e.getMessage()).append("<br />");
        }

        log.error("***** {}", sb.toString(), e);

        return new ModelAndView("error", "error", sb.toString());
    }

} // The End...
