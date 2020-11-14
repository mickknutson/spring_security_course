package io.baselogic.springsecurity.web.handlers;


import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.ServerAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 * CustomServerAuthenticationSuccessHandler
 *
 * @author mickknutson
 * @since chapter14.01
 */
//@Component
@Slf4j
public class CustomServerAuthenticationSuccessHandler implements ServerAuthenticationSuccessHandler {

    /**
     * Invoked when the application authenticates successfully
     * @param webFilterExchange the exchange
     * @param authentication the {@link Authentication}
     * @return a completion notification (success or error)
     */
    public Mono<Void> onAuthenticationSuccess(WebFilterExchange webFilterExchange, Authentication authentication){
        log.debug("*** onAuthenticationSuccess({}, {})", webFilterExchange, authentication);

        return Mono.empty().then();
    }

} // The End...
