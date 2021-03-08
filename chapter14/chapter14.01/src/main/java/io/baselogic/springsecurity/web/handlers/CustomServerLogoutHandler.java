package io.baselogic.springsecurity.web.handlers;

import io.baselogic.springsecurity.domain.EventUserDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.logout.ServerLogoutHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * RedirectingServerLogoutHandler
 *
 * @author mickknutson
 * @since chapter14.01
 */
@Component
@Slf4j
public class CustomServerLogoutHandler implements ServerLogoutHandler {

    public CustomServerLogoutHandler() {
        log.debug("*** CustomServerLogoutHandler ***");
    }

    /**
     * handle logout.
     *
     * @param exchange the exchange
     * @param authentication the {@link Authentication}
     * @return a completion notification (success or error)
     */
    @Override
    public Mono<Void> logout(final WebFilterExchange exchange,
                             final Authentication authentication){

        log.info("*** logout({}, {})", exchange, authentication);

        ServerWebExchange serverWebExchange = exchange.getExchange();
        ServerHttpRequest request = serverWebExchange.getRequest();
        ServerHttpResponse response = serverWebExchange.getResponse();

        response.getCookies().clear();

        // Still not sure why this does not log the user out if I use this
        // and RedirectingServerLogoutSuccessHandler
        return performLogout(authentication).then(
                ReactiveHandlerUtils.sendRedirect(request, response)
        );

//        return Mono.empty().then();

    }

    /**
     * Perform Logout
     * TODO: Need to figure out how to delete JSESSIONID Cookies on server besides:
     * <code>response.getCookies().clear();</code>
     *
     * @param authentication Authentication to logout
     */
    private Mono<String> performLogout(final Authentication authentication){
        log.info("*** performLogout({})", authentication.getName());

        StringBuilder sb = new StringBuilder("Logout Success");

        if (authentication != null) {
            sb.append(" for ").append(authentication.getName());
            authentication.setAuthenticated(false);
            ReactiveSecurityContextHolder.clearContext();
        }

        sb.append(".");

        log.warn(sb.toString());

        return Mono.just(sb.toString());
    }


} // The End...
