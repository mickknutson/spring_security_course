# Chapter 05: JPA-based Authentication


## Section Details

In the previous section, we covered how to leverage Spring Security's built-in JDBC
support. In this section, we will look at the Spring Data project, and how to leverage JPA to
perform authentication against a relational database. We will also explore how to perform
authentication against a document database using MongoDB. This chapter's sample code is
based on the Spring Security setup from Section 4, JDBC-Based Authentication, and has
been updated to refactor out the need for SQL and to use ORM for all database interactions.
During the course of this chapter, we will cover the following topics:

* Some of the basic concepts related to the Spring Data project
* Utilizing Spring Data JPA to authenticate against a relational database
* Utilizing Spring Data MongoDB to authenticate against a document database
* How to customize Spring Security for more flexibility when dealing with Spring Data integration
* Understanding the Spring Data project

The Spring Data project's mission is to provide a familiar and consistent Spring-based
programming model for data access, while still retaining the special traits of the underlying
data provider.

The following are just a few of the powerful features in this Spring Data project:

* Powerful repository and custom object-mapping abstractions
* Dynamic query derivation from repository method names
* Implementation of domain base classes, providing basic properties
* Support for transparent auditing (created and last changed)
* The ability to integrate custom repository code
* Easy Spring integration via Java-based configuration and custom XML namespaces
* Advanced integration with Spring MVC controllers
* Experimental support for cross-store persistence

This project simplifies the use of data access technologies, relational and non-relational
databases, map-reduce frameworks, and cloud-based data services. This umbrella project
contains many sub-projects that are specific to a given database. These projects were
developed by working together with many of the companies and developers that are
behind these exciting technologies. There are also many community maintained modules
and other related modules including JDBC Support and Apache Hadoop.

## Section Chapters

### [Chapter 05.00](./chapter05.00/README.md) (Base line Starting from chapter04.05)

### [Chapter 05.01](./chapter05.01/README.md) (Refactor JDBC implementation to use Spring Data JPA)

### [Chapter 05.02](./chapter05.02/README.md) (Refactor JPA implementation to use Spring Data Mongo DB)

---

## Resources
> * [Project Resources](../docs/resources.md)


---

# [../](../README.md)
