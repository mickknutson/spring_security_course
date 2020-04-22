package io.baselogic.springsecurity.service;

import io.baselogic.springsecurity.annotations.WithMockEventUserDetails;
import io.baselogic.springsecurity.domain.AppUser;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

public class WithMockEventUserDetailsSecurityContextFactory
        implements WithSecurityContextFactory<WithMockEventUserDetails> {
    @Override
    public SecurityContext createSecurityContext(WithMockEventUserDetails mockUser){
        SecurityContext context =
                SecurityContextHolder.createEmptyContext();

        AppUser principal =
                new AppUser();
        Authentication auth =
                new UsernamePasswordAuthenticationToken(principal, "password",
                        null);
//                        principal.getAuthorities());
        context.setAuthentication(auth);
        return context;
    }
} // The End...