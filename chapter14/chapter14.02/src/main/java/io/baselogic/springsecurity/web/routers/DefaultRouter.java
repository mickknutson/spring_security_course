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
 * @since chapter14.01 Refactored for WebFlux
 */
//@Configuration --> Load-time Stereoptype
//@Component --> Run-time Stereotype
public class DefaultRouter {

    @Bean
    public RouterFunction<ServerResponse> route(DefaultHandler defaultHandler) {

        return RouterFunctions
                // URI: "/"
                .route(RequestPredicates.GET("/") // This cannot change at runtime
                        .and(RequestPredicates.accept(MediaType.TEXT_HTML)), defaultHandler::welcome) // This can change at runtime

                // URI: "/login"
                .andRoute(RequestPredicates.GET("/login")
                        .and(RequestPredicates.accept(MediaType.TEXT_HTML)), defaultHandler::login)

                // URI: "/default"
                .andRoute(RequestPredicates.GET("/default")
                        .and(RequestPredicates.accept(MediaType.TEXT_HTML)), defaultHandler::defaultRedirect)


                // URI: "/error"
                .andRoute(RequestPredicates.GET("/error")
                        .and(RequestPredicates.accept(MediaType.TEXT_HTML)), defaultHandler::errors)

                // URI: "/403"
                .andRoute(RequestPredicates.GET("/error/403")
                        .and(RequestPredicates.accept(MediaType.TEXT_HTML)), defaultHandler::error403)
                ;
    }

} // The End...
