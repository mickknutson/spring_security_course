package io.baselogic.springsecurity.web.controllers;

import io.baselogic.springsecurity.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuditEndpoint {

    @Autowired
    private EventService eventService;


    @GetMapping("/trace")
    public String getTraceInfo() {
//        try{
            eventService.findAllEvents();
            return "";//TraceMonitorHolder.getStrategy().printTrace();
//        }
    }

} // The End...
