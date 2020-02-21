# Chapter 04: JDBC-Based Authentication


## Section Details

In the previous chapter, we saw how we can extend Spring Security to utilize our
CalendarDao interface and our existing domain model to authenticate users. In this
chapter, we will see how we can use Spring Security's built-in JDBC support. To keep things
simple, this chapter's sample code is based on our Spring Security setup from Chapter 2,

Getting Started with Spring Security. In this chapter, we will cover the following topics:

* Using Spring Security's built-in JDBC-based authentication support
* Utilizing Spring Security's group-based authorization to make administering users easier
* Learning how to use Spring Security's UserDetailsManager interface
* Configuring Spring Security to utilize the existing CalendarUser schema to authenticate users
* Learning how we can secure passwords using Spring Security's new cryptography module
* Using Spring Security's default JDBC authentication

If your application has not yet implemented security, or if your security infrastructure is
using a database, Spring Security provides out-of-the-box support that can simplify the
solving of your security needs. Spring Security provides a default schema for users,
authorities, and groups. If that does not meet your needs, it allows for the querying and
managing of users to be customized. In the next section, we are going to go through the
basic steps for setting up JDBC authentication with Spring Security.


## Section Chapters

### Chapter 04.01:
> * Base line Starting from chapter03.04

---

### Chapter 04.01:
> * TBD

  **Logging username:** *user1@example.com*
  
  **Password:** *user1*

---

