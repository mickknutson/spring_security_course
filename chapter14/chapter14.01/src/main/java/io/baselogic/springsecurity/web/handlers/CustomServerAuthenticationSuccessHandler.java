package io.baselogic.springsecurity.web.handlers;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.ServerAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * CustomServerAuthenticationSuccessHandler
 *
 * @author mickknutson
 * @since chapter14.01
 */
@Component
@Slf4j
public class CustomServerAuthenticationSuccessHandler implements ServerAuthenticationSuccessHandler {

    public CustomServerAuthenticationSuccessHandler() {
        log.debug("*** CustomServerAuthenticationSuccessHandler ***");
    }

    private String defaultSuccessUrl = "/";

    private boolean alwaysUse;


    /**
     * Invoked when the application authenticates successfully
     * @param webFilterExchange the exchange
     * @param authentication the {@link Authentication}
     * @return a completion notification (success or error)
     */
    public Mono<Void> onAuthenticationSuccess(final WebFilterExchange webFilterExchange,
                                              final Authentication authentication){
        log.debug("*** onAuthenticationSuccess({}, {})", webFilterExchange, authentication);

        ServerWebExchange serverWebExchange = webFilterExchange.getExchange();
        ServerHttpRequest request = serverWebExchange.getRequest();
        ServerHttpResponse response = serverWebExchange.getResponse();

        return ReactiveHandlerUtils.sendRedirect(request, response, defaultSuccessUrl);

    }

    public CustomServerAuthenticationSuccessHandler defaultSuccessUrl(final String defaultSuccessUrl) {
        this.defaultSuccessUrl = defaultSuccessUrl;
        return this;
    }

} // The End...
