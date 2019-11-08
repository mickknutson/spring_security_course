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

1. [Course Reference Project](chapter01/README.md)
2. [Getting Started with Spring Security](chapter02/README.md)


# Building and Running the code examples

Code Software Requirements
=
* JDK 8
* Maven 3.x
* IntelliJ 2019+
* Eclipse Neon+
* Lombok Plugin

Good reference to setup Lombok for Intellij and Eclipse IDE:
-
[https://www.baeldung.com/lombok-ide](https://www.baeldung.com/lombok-ide)



Running Individual projects:
-
* Running the Spring Boot Application: (from chapter root) ./mvn
  spring-boot:run
* Running Tests: (from chapter root) ./mvn verify -e


# TODO's

* Get project to build in Circle-CI
* Get WebDriver tests to work.
* Need to enable SonarQube for all modules
* Integrate CircleCI to [FOSSA](https://fossa.io/docs/integrating-tools/circleci/)



# the end...
