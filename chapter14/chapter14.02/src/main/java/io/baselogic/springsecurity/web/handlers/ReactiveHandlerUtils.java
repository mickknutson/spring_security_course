package io.baselogic.springsecurity.web.handlers;

import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.net.URI;

/**
 * ReactiveHandlerUtils
 * Utilities to support Reactive utilities
 *
 * @author mickknutson
 *
 * @since chapter14.01 Created
 */
public interface ReactiveHandlerUtils {


    /**
     * Send an ServerHttpResponse redirect to root URI.
     * @param serverHttpRequest ServerHttpRequest
     * @param serverHttpResponse ServerHttpResponse
     * @return Mono<Void> by setting the response complete
     */
    static Mono<Void> sendRedirect(final ServerHttpRequest serverHttpRequest,
                                   final ServerHttpResponse serverHttpResponse){

        return sendRedirect(serverHttpRequest, serverHttpResponse, "/");
    }

    /**
     * Send an ServerHttpResponse redirect to uriPath URI.
     * @param serverHttpRequest ServerHttpRequest
     * @param serverHttpResponse ServerHttpResponse
     * @param uriPath to redirect to.
     * @return Mono<Void> by setting the response complete
     */
    static Mono<Void> sendRedirect(final ServerHttpRequest serverHttpRequest,
                                   final ServerHttpResponse serverHttpResponse,
                                   final String uriPath){

        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUri(serverHttpRequest.getURI());

        UriComponents uriComponents = uriBuilder.replacePath(uriPath).build();
        URI uri = uriComponents.toUri();

        serverHttpResponse.setStatusCode(HttpStatus.MOVED_PERMANENTLY);

        serverHttpResponse.getHeaders().setLocation(uri);

        return serverHttpResponse.setComplete();
    }

} // The End...
