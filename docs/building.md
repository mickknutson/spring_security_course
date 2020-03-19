# Building and Running the code examples

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

## Using Compass to access in-memory MongoDB
[https://www.mongodb.com/products/compass](https://www.mongodb.com/products/compass)

Connection:

    mongodb uri localhost:37681


---

# [../](../README.md)
