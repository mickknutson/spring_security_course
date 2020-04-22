package io.baselogic.springsecurity.annotations;

import org.springframework.security.test.context.support.WithUserDetails;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)

@WithUserDetails(value="user1@baselogic.com")
public @interface WithUserDetailsUser1 {

}  // The End...
