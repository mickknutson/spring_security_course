# Chapter 06: Remember-Me Services

## Section Details

In this chapter, we'll add the ability for an application to remember a user even after their
session has expired and the browser is closed. The following topics will be covered in this
chapter:
> *  Discussing what remember-me is
> *  Learning how to use the token-based remember-me feature
> *  Discussing how secure remember-me is, and various ways of making it more secure
> *  Enabling the persistent-based remember-me feature, and how to handle additional considerations for using it
> *  Presenting the overall remember-me architecture
> *  Learning how to create a custom remember-me implementation that is restricted to the user's IP address

### What is remember-me?
A convenient feature to offer frequent users of a website is the remember-me feature. This
feature allows a user to elect to be remembered even after their browser is closed. In Spring
Security, this is implemented through the use of a remember-me cookie that is stored in the
user's browser. If Spring Security recognizes that the user is presenting a remember-me
cookie, then the user will automatically be logged into the application, and will not need to
enter a username or password.

Spring Security provides the following two different strategies that we will discuss in this
chapter:
> *  The first is the token-based remember-me feature, which relies on a
cryptographic signature
> *  The second method, the persistent-based remember-me feature, requires a
data-store (a database)

As we previously mentioned, we will discuss these strategies in much greater detail
throughout this chapter. The remember-me feature must be explicitly configured in order to
enable it. Let's start off by trying the token-based remember-me feature and see how it
affects the flow of the login experience.

## Section Chapters

### [Chapter 06.00](./chapter06.00/README.md) (Base line Starting from chapter05.01)

### [Chapter 06.01](./chapter06.01/README.md) (Basic token-based remember-me configuration)

### [Chapter 06.02](./chapter06.02/README.md) (Advanced token-based remember-me configuration)

### [Chapter 06.03](./chapter06.03/README.md) (Persistent remember-me configuration)

---

## Resources
> * [Project Resources](../docs/resources.md)


---

# [../](../README.md)
