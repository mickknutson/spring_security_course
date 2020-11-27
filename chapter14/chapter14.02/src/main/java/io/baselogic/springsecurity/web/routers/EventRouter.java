package io.baselogic.springsecurity.web.configuration;

import io.baselogic.springsecurity.web.handlers.DefaultHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;


/**
 * XXX
 *
 * @author mickknutson
 *
 * @since chapter14.01 Refactored for WebFLux
 */
//@Configuration
public class EventRouter {

    @Bean
    public RouterFunction<ServerResponse> route(DefaultHandler defaultHandler) {

        return RouterFunctions
                // URI: "/"
                .route(RequestPredicates.GET("/events/")
                        .and(RequestPredicates.accept(MediaType.TEXT_HTML)), defaultHandler::welcome)

                // URI: "/my"
                .andRoute(RequestPredicates.GET("/events/my")
                        .and(RequestPredicates.accept(MediaType.TEXT_HTML)), defaultHandler::login);
    }

} // The End...
