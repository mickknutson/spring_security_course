# Spring Security Course (Module 1)

This project is the example code for the Spring security course on Udemy.com

## BLiNC Event Manager Application

* BLiNC Event Manager Application
* 5.00.00-SNAPSHOT
* [BASE Logic Home](https://baselogic.io)
* [Udemy Course Home](https://udemy.com)
* [Youtube Chanel](https://youtube.com/c/baselogic)
* [GitHub](https://github.com/mickknutson/spring_security_course)
* [codecov](https://codecov.io/gh/mickknutson)

This code has been developed in [Maven](http://maven.apache.org) and has
a sub-project for each chapter milestone.

[Thymeleaf](http://www.thymeleaf.org/doc/tutorials/3.0/usingthymeleaf.html)
has been used as the view templating engine throughout the book


# Section Overview

1. [Course Reference Project](chapter01/README.md)
2. [Getting Started with Spring Security](chapter02/README.md)
3. [Custom Authentication](chapter03/README.md)
4. [JDBC-Based Authentication](chapter04/README.md)
5. [JPA-based Authentication](chapter05/README.md)


## Building and Running the code examples

## Code Software Requirements

* JDK 8+
* Maven 3.x
* IntelliJ 2019+
* Eclipse Neon+
* Lombok Plugin

## Good reference to setup Lombok for Intellij and Eclipse IDE:
[https://www.baeldung.com/lombok-ide](https://www.baeldung.com/lombok-ide)


## Preparing Maven to run offline
    mvn dependency:sources
    mvn dependency:resolve -Dclassifier=javadoc
    mvn dependency:go-offline

or :

    mvn dependency:sources dependency:resolve -Dclassifier=javadoc dependency:go-offline


## Running Individual projects

To run each project in this chapter, run the following command from the
milestone directory:

    ~ ./mvn spring-boot:run

then open a browser to:
[http://localhost:8080](http://localhost:8080)


## Using the H2 DB Web Admin Console

After the application is running, the H2 admin Servlet will be running
and the console can be accessed via the following URL:
[http://localhost:8080/admin/h2](http://localhost:8080/admin/h2)


# Sections for next course module
>* Remember-Me Services
>* Client Certificate Authentication with TLS
>* Fine-Grained Access Control
>* Access Control Lists
>* Custom Authorization
>* Session Management
>* Additional Spring Security Features


# TODO's

* Get Selenium / WebDriver tests to work.
* Integrate CircleCI to [FOSSA](https://fossa.io/docs/integrating-tools/circleci/)
* fix favicon.ico not rendering
* Refactor WebMvcConfig to remove WebMvcConfigurerAdapter
* My [Competition](https://www.udemy.com/course/learning-path-spring-secure-your-apps-with-spring-security/).
* Add more tests for DomainUsernamePasswordAuthenticationFilter
* [https://www.mongodb.com/products/compass](https://www.mongodb.com/products/compass) mongodb uri localhost:37681

# License
Code is under the [BSD 2-clause "Simplified" License](LICENSE.txt).

# Badges...

We don't need stinking badges...

[![CircleCI](https://circleci.com/gh/mickknutson/spring_security_course.svg?style=svg)](https://circleci.com/gh/mickknutson/spring_security_course)

[![SonarCloud](https://sonarcloud.io/images/project_badges/sonarcloud-white.svg)](https://sonarcloud.io/dashboard?id=mickknutson_spring_security_course)

[![Lines of Code](https://sonarcloud.io/api/project_badges/measure?project=mickknutson_spring_security_course&metric=ncloc)](https://sonarcloud.io/dashboard?id=mickknutson_spring_security_course)
[![Security Rating](https://sonarcloud.io/api/project_badges/measure?project=mickknutson_spring_security_course&metric=security_rating)](https://sonarcloud.io/dashboard?id=mickknutson_spring_security_course)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=mickknutson_spring_security_course&metric=alert_status)](https://sonarcloud.io/dashboard?id=mickknutson_spring_security_course)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=mickknutson_spring_security_course&metric=coverage)](https://sonarcloud.io/dashboard?id=mickknutson_spring_security_course)
[![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=mickknutson_spring_security_course&metric=sqale_rating)](https://sonarcloud.io/dashboard?id=mickknutson_spring_security_course)

[![FOSSA Status](https://app.fossa.com/api/projects/git%2Bgithub.com%2Fmickknutson%2Fspring_security_course.svg?type=shield)](https://app.fossa.com/projects/git%2Bgithub.com%2Fmickknutson%2Fspring_security_course?ref=badge_shield)

https://tenor.com/bc2EA.gif
