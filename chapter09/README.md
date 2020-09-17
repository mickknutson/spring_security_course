# Chapter 09: Advanced Authorization


## Section Details

In this chapter, we will first examine two ways to implement fine-grained
authorization—authorization that may affect portions of a page of the application. 
Next, we will look at Spring Security's approach to securing the business tier through method annotation and the use of interface-based proxies to accomplish AOP. Then, we will review
an interesting capability of annotation-based security that allows for role-based filtering on
collections of data. Last, we will look at how class-based proxies differ from interface-based
proxies.

During the course of this chapter, we'll cover the following topics:
> * Configuring and experimenting with different methods of performing in-page authorization checks on content, given the security context of a user request
> * Performing configuration and code annotation to make caller pre-authorization a key part of our application's business-tier security
> * Several alternative approaches to implement method-level security, and reviewing the pros and cons of each type
> * Implementing data-based filters on collections and arrays using method-level annotations
> * Implementing method-level security on our Spring MVC controllers to avoid configuring **antMatcher()** methods and **_<intercept-url>_** elements


## Section Chapters

### [Chapter 09.00](./chapter09.00/README.md) (Base line Starting from chapter05.01)

### [Chapter 09.01](./chapter09.01/README.md) (Conditional rendering with the Thymeleaf Spring Security tag library)

### [Chapter 09.02](./chapter09.02/README.md) (Using controller logic to conditionally render content)

### [Chapter 09.03](./chapter09.03/README.md) (Using the WebInvocationPrivilegeEvaluator class)

---

## Resources
> * [https://spring.io/projects/spring-data](https://spring.io/projects/spring-data)
> * [https://spring.io/projects/spring-data-jpa](https://spring.io/projects/spring-data-jpa)
> * [http://www.h2database.com/html/main.html](http://www.h2database.com/html/main.html)

---

# [../](../README.md)
