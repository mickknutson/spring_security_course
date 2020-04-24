package io.baselogic.springsecurity.annotations;

import io.baselogic.springsecurity.userdetails.WithMockEventUserDetailsSecurityContextFactory;
import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)

@WithSecurityContext(factory =
        WithMockEventUserDetailsSecurityContextFactory.class)
public @interface WithMockEventUserDetails {

    int id() default 0;

    String username() default "user";

    String password() default "password";

    String name() default "USER";

    String[] roles() default { "USER" };

}  // The End...
