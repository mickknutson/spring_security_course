package io.baselogic.springsecurity.web.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

/**
 * ErrorController
 *
 * @since chapter01.00
 * @author mickknutson
 */
@ControllerAdvice
@Slf4j
public class ErrorController {

    @ExceptionHandler(Throwable.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ModelAndView handleInternalServerError(final Throwable e) {

        final String BREAK = "<br />";

        StringBuilder sb = new StringBuilder(1_000);
        sb.append("<h2>Unknown error</h2>").append(BREAK);
        sb.append("Exception during execution of SpringSecurity application:").append(BREAK);

        if(e != null){
            sb.append(e.getMessage()).append(BREAK);
            sb.append("\n__________________________________________________\n");
            sb.append("root cause: ").append(e.getCause());
            sb.append("\n__________________________________________________\n");
        }

        log.error("***** {}", sb.toString(), e);

        return new ModelAndView("error", "error", sb.toString());
    }

} // The End...
