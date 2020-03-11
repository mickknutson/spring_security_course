# Chapter 04.02

## JDBC-Based Authentication
This chapter is the chapter BASE and continues from the previous  
chapter section, from here, we build on top of this base.

## Tasks

### No Tasks
This section does not have any tasks.
This section is a baseline from:
>* TBD


---

# [../](../)


* The UserDetailsManager interface
* Group-based access control
* Configuring group-based access control
* Configuring JdbcUserDetailsManager to use groups
* Utilizing GBAC JDBC scripts
* The group-based schema
* Group authority mappings




## Notes:

By using the standard AuthenticationManagerBuilder to use

    auth.jdbcAuthentication()
        .dataSource(dataSource)
        .groupAuthoritiesByUsername(CUSTOM_GROUP_AUTHORITIES_BY_USERNAME_QUERY)

returns a 'org.springframework.security.core.userdetails.User' which does not have
a .getName() method, thus for now, I put the instanceof check in the 'header.html' template:

    <ul class="nav navbar-nav pull-right" sec:authorize="isAuthenticated()">
    <li>
        <p class="navbar-text">Welcome
        <div th:if="${#authentication.getPrincipal().class.name == 'org.springframework.security.core.userdetails.User'}">
            <span th:text="${#authentication.getPrincipal().getUsername()}"></span>
        </div>
        <div th:unless="${#authentication.getPrincipal().class.name == 'org.springframework.security.core.userdetails.User'}">
            <span th:text="${#authentication.getPrincipal().getName()}"></span>
        </div>
        </p>
    </li>

This can be changed by creating a custom 'EventUserAuthenticationProvider' or 'EventUserDetailsService'
