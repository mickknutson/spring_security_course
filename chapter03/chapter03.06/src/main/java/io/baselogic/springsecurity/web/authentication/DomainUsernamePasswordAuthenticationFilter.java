package io.baselogic.springsecurity.web.authentication;

import io.baselogic.springsecurity.authentication.DomainUsernamePasswordAuthenticationToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * An extension to the existing {@link UsernamePasswordAuthenticationFilter} that
 * obtains a domain parameter and then creates a {@link DomainUsernamePasswordAuthenticationToken}.
 *
 * @author mickknutson
 * @since chapter03.06 Created Class
 *
 */
@Slf4j
public final class DomainUsernamePasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    @Autowired
    public DomainUsernamePasswordAuthenticationFilter(final AuthenticationManager authenticationManager){
        super.setAuthenticationManager(authenticationManager);
    }

    @Override
    public Authentication attemptAuthentication(final HttpServletRequest request,
                                                final HttpServletResponse response)
            throws AuthenticationException {

        log.info("attemptAuthentication");
        log.info("request.getMethod(): {}", request.getMethod());

        if (!request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        }

        String username = obtainUsername(request);
        String password = obtainPassword(request);

        String domain = request.getParameter("domain");

        log.info("Create new DomainUsernamePasswordAuthenticationToken");
        log.info("based on credentials(username, password, domain): ({}, {}, {})", username, password, domain);

        DomainUsernamePasswordAuthenticationToken authRequest
                = new DomainUsernamePasswordAuthenticationToken(username, password, domain);

        setDetails(request, authRequest);

        return this.getAuthenticationManager().authenticate(authRequest);
    }

    @Override
    @Autowired
    public void setAuthenticationManager(final AuthenticationManager authenticationManager) {
        super.setAuthenticationManager(authenticationManager);
    }


} // The End...