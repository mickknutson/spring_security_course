# Chapter 09.04
## Advanced Authorization

Securing the business tier with prePostEnabled enabled

## Tasks

### Update Project files

> * Update *[EventService.java](./src/main/java/io/baselogic/springsecurity/service/EventService.java)* to add @PreAuthorize("hasRole('ADMIN')").
> * Update *[SecurityConfig.java](./src/main/java/io/baselogic/springsecurity/configuration/SecurityConfig.java)* to add @EnableGlobalMethodSecurity(prePostEnabled = true).
> * Updated *[AdvancedAuthorizationTests.java](./src/test/java/io/baselogic/springsecurity/web/controllers/AdvancedAuthorizationTests.java)* to add AdvancedAuthorizationTests.
> * Updated *[DefaultEventServiceTests.java](./src/test/java/io/baselogic/springsecurity/service/DefaultEventServiceTests.java)* to update DefaultEventServiceTests.


---

# [../](../)
