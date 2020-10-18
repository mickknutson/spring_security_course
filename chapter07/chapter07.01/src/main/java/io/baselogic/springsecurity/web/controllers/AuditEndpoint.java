package io.baselogic.springsecurity.web.controllers;

import io.baselogic.springsecurity.audit.AuditContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class AuditEndpoint {

    // get put post delete options head
    @GetMapping("/trace")
    public String getTraceInfo() {
        String trace = AuditContextHolder.getContext().printTrace();
        log.info("**** getTraceInfo(): {}", trace);
        return trace;
    }

} // The End...
