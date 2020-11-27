# Chapter 14.01

## Reactive Spring Security
This chapter refactors chapter14.00 which is based on Thymeleaf, WebMvc, and MongoDb.
In this chapter we use Thymeleaf, and MongoDb, but we use reactive-spring and webflux.

## Tasks

### No Tasks
This section does not have any tasks.
This section is a baseline from:
> * [chapter07.01](../../chapter05/chapter05.02/README.md)


## Open issues

> * Service & Dao layer validation not implemented.
> > There are validation annotations such as @NotNull and @Valid.
> > Spring Webflux implements a functional-style web layer, which is based on router functions and handlers.  
> > This is non-blocking API and things are different from what we know from legacy approaches. When we need to validate an input, we actually need to do it manually.
> > Might consider https://github.com/making/yavi for future validation.
> > https://www.mednikov.tech/validation-for-spring-webflux-apis-with-yavi/

---

# [../](../)
