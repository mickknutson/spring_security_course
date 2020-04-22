package io.baselogic.springsecurity.annotations;

import org.springframework.security.test.context.support.WithMockUser;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithMockUser(username = "admin1@baselogic.com", password = "admin1", roles = {"USER", "ADMIN"})
public @interface WithMockAdmin1 {

}  // The End...
