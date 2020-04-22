# Chapter 00: General Security overview

## General Security overview
This is the Spring Security overview reference project for
the rest of the course.

### Authentication
Authentication is one of two key security concepts that you must internalize when
developing secure applications (the other being authorization). Authentication identifies
who is attempting to request a resource.


### Authorization
Inappropriate or non-existent use of authorization
Authorization is the second of two core security concepts that are crucial in implementing
and understanding application security. Authorization uses the information that was
validated during authentication to determine whether access should be granted to a
particular resource.


### Database credential security
Database credentials are not secure or easily accessible. Through the examination of the
application source code and configuration files, the auditors noted that user passwords
were stored in plain text in the configuration files, making it very easy for a malicious user
with access to the server to gain access to the application.

As the application contains personal and financial data, a rogue user being able to access
any data could expose the company to identity theft or tampering. Protecting access to the
credentials used to access the application should be a top priority for us, and an important
first step is ensuring that one point of failure in security does not compromise the entire
system.

### Sensitive information
Personally identifiable or sensitive information is easily accessible or unencrypted. The
auditors noted that some significant and sensitive pieces of data were completely
unencrypted or masked anywhere in the system. Fortunately, there are some simple design
patterns and tools that allow us to protect this information securely, with annotation-based
AOP support in Spring Security.


### Transport-level security
There is insecure transport-level protection due to lack of SSL encryption.
While, in the real world, it's unthinkable that an online application containing private
information would operate without SSL protection.  
SSL protection ensures that communication between the browser client
and the web application server are secure against many kinds of tampering and snooping.



### Spring Security to the rescue
With Spring Security, we'll be able to make the following changes to increase our
application's security:
> * Segment users of the system into user classes
> * Assign levels of authorization to user roles
> * Assign user roles to user classes
> * Apply authentication rules globally across application resources
> * Apply authorization rules at all levels of the application architecture
> * Prevent common types of attacks intended to manipulate or steal a user's session


### Why Spring Security?
Spring Security exists to fill a gap in the universe of Java third-party libraries, much as the
Spring Framework originally did when it was first introduced.

Standards such as Java Authentication and Authorization Service (JAAS) or Java EE Security do offer some ways
of performing some of the same authentication and authorization functions, but Spring
Security is a winner because it includes everything you need to implement a top-to-bottom
application security solution in a concise and sensible way.

Additionally, Spring Security appeals to many, because it offers out-of-the-box integration
with many common enterprise authentication systems; so it's adaptable to most situations
with little effort (beyond configuration) on the part of the developer.

It's in wide use because there's really no other mainstream framework quite like it!



---

# [../](../README.md)
