# Chapter 05.02

## Refactoring from an RDBMS to a document database
Refactoring from an RDBMS to a document database using MongoDB

## Tasks

### Updating project dependencies
> * REMOVE spring-boot-starter-data-jpa in [pom.xml](./pom.xml)
> * REMOVE com.h2database:h2 in [pom.xml](./pom.xml)
> * ADD spring-boot-starter-data-mongodb in [pom.xml](./pom.xml)
> * Add de.flapdoodle.embed.mongo in [pom.xml](./pom.xml)

### Reconfiguring the database configuration in MongoDB

> * Add Mongo configuration in [application.yml](src/main/resources/application.yml)
> * Remove schema.sql and data.sql from [src/main/resources/](src/main/resources/)

### Initializing the MongoDB database
> * Create MongoData Initializer

### Mapping domain objects with MongoDB
> * Map Role to MongoDB
> * Map AppUser to MongoDB
> * Map Event to MongoDB

### Refactor JPA repositories to MongoDB
> * Refactor RoleRepository to extend MongoRepository
> * Refactor AppUserRepository to extend MongoRepository
> * Refactor EventRepository to extend MongoRepository
> * Refactor EventRepository findByUser() method to use JSON @Query

### Refactor DAO Services from JPA to MongoDB
> * Remove JPA DAO implementations
> * Create Mongo DAO implementations


---

# [../](../)
