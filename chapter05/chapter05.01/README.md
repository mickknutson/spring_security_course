# Chapter 05.01
## JPA-based Authentication

## Tasks

### Initializing the database

> * Remove directory and all files in *[~/resources/database/**](./src/main/resources/)
> * Add empty file *[data.sql](./src/main/resources/data.sql)*
> * Update *[data.sql](./src/main/resources/data.sql)*
> * Add JPA configuration to *[./src/main/resources/application.yml](./src/main/resources/application.yml)*

### Refactoring from SQL to ORM

Mapping domain objects using JPA

> * Add JPA Mapping to *'Event.java'*
> * create a Role.java file
> * Add JPA Mapping to *'AppUser.java'* for *'Role.java'*

### Spring Data repositories
Creating Spring-Data Repository Objects

> * Add AppUserRepository.java
> * Add RoleRepository.java
> * Add EventRepository.java

### Data access objects
Modifying DAO Objects

> * Create JpaEventDao.java
> * Delete JdbcEventDao.java
> * Create JpaUserDao.java
> * Delete JdbcUserDao.java

### Application services
Modify Service Objects

> * Update DefaultEventService.java
> * Update UserDetailsServiceImpl.java

### Update Misc Code
Need to modify some code used for Testing and test fixtures

> * Fix TestUtils.java
> * Fix DefaultEventServiceTests.java
> * Fix JdbcEventDaoTests.java
> * Remove EventRowMapper.java
> * Remove UserRowMapper.java
> * Remove spring.datasource.schema list in *[application.yml](./src/main/resources/application.yml)*


---

# [../](../)
