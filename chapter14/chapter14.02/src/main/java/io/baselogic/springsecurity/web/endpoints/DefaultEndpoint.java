package io.baselogic.springsecurity.web.endpoints;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.security.Principal;

import static org.springframework.web.reactive.function.server.ServerResponse.*;
import static reactor.core.publisher.Mono.*;

/**
 * DefaultEndpoint
 * Converted from DefaultController
 *
 * @author mickknutson
 *
 * @since chapter14.01 Converted from Controller
 */
@RestController
@Slf4j
public class DefaultEndpoint {

//    @GetMapping("/default")
//    public String defaultAfterLogin(HttpServletRequest request) {
//
//        if (request.isUserInRole("ADMIN")) {
//            return "redirect:/events/";
//        }
//        return "redirect:/";
//    }

    @GetMapping("/default")
    public String defaultAfterLogin(Mono<Principal> principal) {

        Principal user = principal.block();
        log.info("principal: [{}]", user);

//        if (request.isUserInRole("ADMIN")) {
//            return "redirect:/events/";
//        }

//        httpServletResponse.setHeader("Location", projectUrl);
//        httpServletResponse.setStatus(302);

        return "redirect:/";
    }

    // see DefaultServerResponseBuilder

//    @GetMapping("/default")
//    public Mono<ServerResponse> create(Mono<Principal> principal) throws Exception{
//        return just(principal)
//                .map(created(new URI("/")).build());
//    }

} // The End...
