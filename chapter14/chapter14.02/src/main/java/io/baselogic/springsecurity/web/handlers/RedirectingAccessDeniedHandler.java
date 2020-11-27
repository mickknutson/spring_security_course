package io.baselogic.springsecurity.web.handlers;


import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.server.authorization.ServerAccessDeniedHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * RedirectingAccessDeniedHandler
 *
 * @author mickknutson
 * @since chapter14.01
 */
@Component
@Slf4j
public class RedirectingAccessDeniedHandler implements ServerAccessDeniedHandler {


    public RedirectingAccessDeniedHandler() {
        log.info("*** RedirectingAccessDeniedHandler ***");
    }

    /**
     * Handle ServerAccessDeniedHandler
     *
     * @param serverWebExchange ServerWebExchange
     * @param e AccessDeniedException
     * @return Mono<Void>
     */
    @Override
    public Mono<Void> handle(ServerWebExchange serverWebExchange, AccessDeniedException e){

        log.debug("*** handle({}, {})", serverWebExchange, e.getMessage());

        log.info("*** Throw AccessDeniedException ***");
        ServerHttpRequest request = serverWebExchange.getRequest();
        ServerHttpResponse response = serverWebExchange.getResponse();

        return ReactiveHandlerUtils.sendRedirect(request, response, "/error/403")
                .then();
    }

} // The End...
