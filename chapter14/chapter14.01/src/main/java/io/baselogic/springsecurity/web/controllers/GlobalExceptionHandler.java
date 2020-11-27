package io.baselogic.springsecurity.web.controllers;

import io.baselogic.springsecurity.domain.Event;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import java.util.stream.Stream;

/**
 * GlobalExceptionHandler
 *
 * @author mickknutson
 *
 * @since chapter14.01 Refactored for WebFlux
 */
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {


    @GetMapping("/error/{code}")
    public String showError(final Model model, final @PathVariable String code) {
        log.error("* showError: {}", code);
        model.addAttribute("error", code);
        log.error("show: error/{}", code);
        return "error/"+code;
    }


    @ExceptionHandler(TypeMismatchException.class)
    public String handleTypeMismatchException(final Model model,
                                              final TypeMismatchException e) {
        log.error("* handleTypeMismatchException: {}", e);

        StringBuilder sb = new StringBuilder(1_00);
        sb.append("Invalid value '").append(e.getValue()).append("'. ");
        sb.append("Required Type '").append(e.getRequiredType()).append("'. ");
        sb.append("Cause '").append(e.getCause()).append("'. \n");

        model.addAttribute("error", sb.toString());

        log.error("***** Message: [{}], Exception: [{}]", sb.toString(), e);
        log.info("Stack", e.getStackTrace());

        return "error/error";
    }

    @ExceptionHandler(WebExchangeBindException.class)
    public String handleWebExchangeBindException(final Model model,
                                                 final WebExchangeBindException e) {
        log.error("* handleTypeMismatchException: {}", e);

        StringBuilder sb = new StringBuilder(1_00);
        sb.append("All WebExchangeBindException Errors '").append(e.getAllErrors()).append("'. ");
        sb.append("Global Errors '").append(e.getGlobalErrors()).append("'. ");
        sb.append("Cause '").append(e.getCause()).append("'. \n");

        log.error("***** Message: [{}], Exception: [{}]", sb.toString(), e);
        log.info("Stack", e.getStackTrace());

        model.addAttribute("error", sb.toString());

        return "error/error";
    }

    @ExceptionHandler(AccessDeniedException.class)
    public String accessDeniedException(final Model model, final AccessDeniedException e) {
        log.error("* accessDeniedException: {}", e);

        String result = getExceptionMessage(e);

        model.addAttribute("error", result);

        return "redirect:/error/403";
    }


    @ExceptionHandler(Throwable.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleException(final Model model, final Throwable e) {
        log.error("* handleException: {}", e);

        String result = getExceptionMessage(e);

        model.addAttribute("error", result);

        return "redirect:/error/5xx";
    }

    public String getExceptionMessage(final Throwable e) {

        StringBuilder sb = new StringBuilder(1_00);
        sb.append("* Unknown error: Exception during execution of SpringSecurity application: ");

        sb.append("Message: ").append(e.getMessage()).append(", \n");

        if(e.getCause() != null) {
            sb.append("Root cause: ").append(e.getCause()).append(", \n");
        }

        StackTraceElement[] ste2 = e.getStackTrace();


        sb.append("Stack (").append(ste2.length).append("): \n");

        Stream.of(ste2).limit(10).forEach( s -> sb.append(s).append("\n"));

        log.error("***** Exception: [{}]", sb.toString());


        return sb.toString();
    }

} // The End...
