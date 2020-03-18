# Chapter 05.02

## Refactoring from an RDBMS to a document database
Refactoring from an RDBMS to a document database using MongoDB

## Tasks

### Updating project dependencies
> * REMOVE spring-boot-starter-data-jpa in [pom.xml](./pom.xml)
> * REMOVE spring-boot-starter-data-mongodb in [pom.xml](./pom.xml)
> * ADD spring-boot-starter-data-mongodb in [pom.xml](./pom.xml)
> * Add de.flapdoodle.embed.mongo in [pom.xml](./pom.xml)

### Reconfiguring the database configuration in MongoDB

> * Add Mongo configuration in [application.yml](src/main/resources/application.yml)

### Initializing the MongoDB database
> * Create MongoData Initializer

### Mapping domain objects with MongoDB
> * TBD

### Refactor JPA repositories to MongoDB
> * TBD

### Refactor DAO Services from JPA to MongoDB
> * Remove JPA DAO implementations
> * Create Mongo DAO implementations


---

# [../](../)
