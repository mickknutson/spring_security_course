package io.baselogic.springsecurity.web.handlers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.util.UriBuilder;
import reactor.core.publisher.Mono;

import java.net.URI;

//@Component
@Slf4j
public class DefaultHandler {

    public Mono<ServerResponse> welcome(ServerRequest request) {
        log.info("* welcome: [{}]", request);
        return ServerResponse
                .ok()
                .contentType(MediaType.TEXT_HTML)
                .render("index");
    }

    public Mono<ServerResponse> login(ServerRequest request) {
        log.info("* login: [{}]", request);
        return ServerResponse
                .ok()
                .contentType(MediaType.TEXT_HTML)
                .render("login");
    }

    public Mono<ServerResponse> logout(ServerRequest request) {
        log.info("* logout: [{}]", request);
        return ServerResponse
                .ok()
                .contentType(MediaType.TEXT_HTML)
                .render("logout");
    }

    public Mono<ServerResponse> defaultRedirect(ServerRequest request) {
        log.info("* defaultRedirect: [{}]", request);

//        Principal principal;
        request.principal().subscribe(p -> log.info("principal: [{}]", p));

        UriBuilder uriBuilder = request.uriBuilder();

        URI uri = uriBuilder.replacePath("/events/").build();

        return ServerResponse
                .temporaryRedirect(uri).build();
    }

    public Mono<ServerResponse> errors(ServerRequest request) {
        log.info("* error: [{}]", request);

        UriBuilder uriBuilder = request.uriBuilder();

        URI uri = uriBuilder.replacePath("/error/403").build();

        return ServerResponse
                .temporaryRedirect(uri)
                .build();
    }

    public Mono<ServerResponse> error403(ServerRequest request) {
        log.info("* error403: [{}]", request);

        UriBuilder uriBuilder = request.uriBuilder();

        URI uri = uriBuilder.replacePath("/error/403").build();

        return ServerResponse
                .ok()
                .contentType(MediaType.TEXT_HTML)
                .render("error/403");
    }

} // The End...
