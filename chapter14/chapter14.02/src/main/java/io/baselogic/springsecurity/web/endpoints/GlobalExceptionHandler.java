package io.baselogic.springsecurity.web.endpoints;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(TypeMismatchException.class)
    public ResponseStatusException handleTypeMismatchException(final TypeMismatchException e) {

        StringBuilder sb = new StringBuilder(1_00);
        sb.append("Invalid value '").append(e.getValue()).append("'. ");
        sb.append("Required Type '").append(e.getRequiredType()).append("'. ");

        log.error("***** Message: [{}], Exception: [{}]", sb.toString(), e);

        return new ResponseStatusException(HttpStatus.BAD_REQUEST, sb.toString(), e);
    }

    @ExceptionHandler(WebExchangeBindException.class)
    public WebExchangeBindException handleWebExchangeBindException(final WebExchangeBindException e) {

        StringBuilder sb = new StringBuilder(1_00);
        sb.append("All WebExchangeBindException Errors '").append(e.getAllErrors()).append("'. ");
        sb.append("Global Errors '").append(e.getGlobalErrors()).append("'. ");

        log.error("***** Message: [{}], Exception: [{}]", sb.toString(), e);

        return e;
    }

} // The End...
