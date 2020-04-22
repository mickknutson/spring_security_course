package io.baselogic.springsecurity.annotations;

import io.baselogic.springsecurity.service.WithMockEventUserDetailsSecurityContextFactory;
import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)

@WithSecurityContext(factory =
        WithMockEventUserDetailsSecurityContextFactory.class)
public @interface WithMockEventUserDetails {

    String username() default "user1@baselogic.com";

    String name() default "user1";

}  // The End...
