# Chapter 05.01
## JPA-based Authentication

## Tasks

### Reconfigure the database to support JPA

> * Remove directory and all files in *[~/resources/database/**](./src/main/resources/)
> * Add empty file *[data.sql](./src/main/resources/data.sql)*
> * Update *[data.sql](./src/main/resources/data.sql)*
> * Add JPA configuration to *[./src/main/resources/application.yml](./src/main/resources/application.yml)*

### Refactoring from SQL to ORM
Mapping domain objects using JPA

> * Add JPA Mapping to *'Event.java'*
> * create a Role.java file
> * Add JPA Mapping to *'AppUser.java'* for *'Role.java'*

### Creating Spring-Data Repository Objects

> * Add AppUserRepository.java
> * Add RoleRepository.java
> * Add EventRepository.java

### Migrate DAO Services from JDBC to JPA

> * Create JpaEventDao.java
> * Delete JdbcEventDao.java
> * Create JpaUserDao.java
> * Delete JdbcUserDao.java

### Updating Application Service-Layer Objects

> * Update DefaultEventService.java
> * Update EventUserDetailsService.java

### Update Test and other Misc Code
Need to modify some code used for Testing and test fixtures

> * Fix TestUtils.java
> * Fix DefaultEventServiceTests.java
> * Fix JdbcEventDaoTests.java
> * Remove EventRowMapper.java
> * Remove UserRowMapper.java
> * Remove spring.datasource.schema list in *[application.yml](./src/main/resources/application.yml)*

> * Execute/Run the updated code and test


---

# [../](../README.md)

