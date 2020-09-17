package io.baselogic.springsecurity.annotations;

import org.springframework.security.test.context.support.WithMockUser;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithMockUser(username="user1@baselogic.com", password="user1", roles="USER")
public @interface WithMockUser1 {

}  // The End...
