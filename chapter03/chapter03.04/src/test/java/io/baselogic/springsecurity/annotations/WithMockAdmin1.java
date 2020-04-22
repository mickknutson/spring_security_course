package io.baselogic.springsecurity.annotations;

import org.springframework.security.test.context.support.WithMockUser;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithMockUser(value="admin1", roles={"USER", "ADMIN"})
public @interface WithMockAdmin1 {

}  // The End...
