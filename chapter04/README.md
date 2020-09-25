# Chapter 04: JDBC-Based Authentication


## Section Details
In the previous section, we saw how we can extend Spring Security to utilize our
AppUserDao interface and our existing domain model to authenticate users. In this
section, we will see how we can use Spring Security's built-in JDBC support.

In this section, we will cover the following topics:

* Understand Spring Security's built-in JDBC-based authentication support
* Utilizing Spring Security's group-based authorization to make administering users easier
* Learning how to use Spring Security's UserDetailsManager interface
* Configuring Spring Security to utilize the existing AppUser schema to authenticate users
* Learning how we can secure passwords using Spring Security's new cryptography module
* Using Spring Security's default JDBC authentication

If your application has not yet implemented security, or if your security infrastructure is
using a database, Spring Security provides out-of-the-box support that can simplify the
solving of your security needs. Spring Security provides a default schema for users,
authorities, and groups. If that does not meet your needs, it allows for the querying and
managing of users to be customized. In the next section, we are going to go through the
basic steps for setting up JDBC authentication with Spring Security.


## Section Chapters

### [Chapter 04.00](./chapter04.00/) (Base line Starting from [chapter03.04](./../chapter03/chapter03.04/))

### [Chapter 04.01](./chapter04.01/) (Configuring Jdbc Support)

### [Chapter 04.02](./chapter04.02/) (Configuring group-based access control)

### [Chapter 04.03](./chapter04.03/) (Support for a custom schema)

### [Chapter 04.04](./chapter04.04/) (Configuring secure passwords)


---

## Resources
> * [Project Resources](../docs/resources.md)


---

# [../](../README.md)
