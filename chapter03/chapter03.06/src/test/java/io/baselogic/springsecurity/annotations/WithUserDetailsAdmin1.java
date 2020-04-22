package io.baselogic.springsecurity.annotations;

import org.springframework.security.test.context.support.WithUserDetails;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)

@WithUserDetails(value="admin1@baselogic.com")
public @interface WithUserDetailsAdmin1 {

}  // The End...
