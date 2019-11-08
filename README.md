# README

This README would normally document whatever steps are necessary 
to get your application up and running.

## BLiNC Event Manager Application

* BLiNC Event Manager Application
* 4.00.00-SNAPSHOT
* [BASE Logic Home](https://baselogic.io)
* [Udemy Course Home](https://udemy.com)
* [BitBucket](https://bitbucket.org/mickknutson/jbcpcalendar/)
* [codecov](https://codecov.io/gh/mickknutson)

This code has been developed in [Maven](http://maven.apache.org) and has
a sub-project for each chapter milestone.
[Thymeleaf](http://www.thymeleaf.org/doc/tutorials/3.0/usingthymeleaf.html)
has been used as the view templating engine throughout the book


### Chapters
***

1. [Anatomy of an Unsafe Application](chapter01/README.md)
2. [Getting Started with Spring Security](chapter02/README.md)
3. [Custom Authentication](chapter03/README.md)
4. [JDBC-based Authentication](chapter04/README.md)
5. [Authentication with Spring-Data](chapter05/README.md)
6. LDAP Directory Services
7. Remember-me Services


# Building and Running the code examples

Code Software Requirements
=
* JDK 8
* Maven 3.x
* IntelliJ 2019+
* Eclipse Neon+



Running Individual projects:
-
* Running the Spring Boot Application: (from chapter root) ./mvn
  spring-boot:run
* Running Tests: (from chapter root) ./mvn verify -e


# TODO's
* From Chapter15, add favicon's images to all projects.
* Get project to build in Circle-CI
* Get WebDriver tests to work.
* Need to enable SonarQube for all modules
* Integrate CircleCI to [FOSSA](https://fossa.io/docs/integrating-tools/circleci/)



# the end...
