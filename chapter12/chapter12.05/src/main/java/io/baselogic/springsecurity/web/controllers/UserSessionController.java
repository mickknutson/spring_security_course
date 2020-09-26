package io.baselogic.springsecurity.web.controllers;

import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Allows users to view the active sessions associated to their account and remove sessions that should no longer be active.
 *
 * @author Mick Knutson
 *
 * @since chapter12.05 Created
 *
 */
@Controller
@RequestMapping("/user")
@Slf4j
public class UserSessionController {

    private final SessionRegistry sessionRegistry;

    @Autowired
    public UserSessionController(final SessionRegistry sessionRegistry) {
        if (sessionRegistry == null) {
            throw new IllegalArgumentException("sessionRegistry cannot be null");
        }
        this.sessionRegistry = sessionRegistry;
    }

    @GetMapping("/sessions/")
    public String sessions(final Authentication authentication, final ModelMap model, final RedirectAttributes redirectAttrs) {
        if(authentication == null){
            redirectAttrs.addFlashAttribute("message", "Authentication is null");
            return "redirect:/error";

        }
        List<SessionInformation> sessions = sessionRegistry.getAllSessions(authentication.getPrincipal(),
                false);
        model.put("userSessions", sessions);
        return "user/sessions";
    }

    /**
     * Remove User Session
     *
     * @param sessionId
     * @param redirectAttrs
     * @return
     */
    @DeleteMapping("/sessions/{sessionId}")
    public String removeSession(final @PathVariable String sessionId, final RedirectAttributes redirectAttrs) {
        SessionInformation sessionInformation = sessionRegistry.getSessionInformation(sessionId);

        if(sessionInformation != null) {
            sessionInformation.expireNow();
        }

        redirectAttrs.addFlashAttribute("message", "Session was removed");
        return "redirect:/user/sessions/";
    }

} // The End...
