# Chapter 12: HTTP Session Management


## Section Details

This chapter discusses Spring Security's session management functionality. It starts off with
an example of how Spring Security defends against session fixation. We will then discuss
how concurrency control can be leveraged to restrict access to software licensed on a per-user
basis. We will also see how session management can be leveraged for administrative
functions. Last, we will explore how HttpSession is used in Spring Security and how we
can control its creation.

The following is a list of topics that will be covered in this chapter:
> * Session management/session fixation
> * Concurrency control
> * Managing logged in users
> * How HttpSession is used in Spring Security and how to control creation
> * How to use the DebugFilter class to discover where HttpSession was created

### todo: Look at:
* httpPublicKeyPinning
* contentSecurityPolicy

## Section Chapters

### [Chapter 12.00](./chapter12.00/README.md) (Base line Starting from chapter05.01)

### [Chapter 12.01](./chapter12.01/README.md) (Understanding session fixation attacks)

### [Chapter 12.02](./chapter12.02/README.md) (Restricting the number of concurrent sessions per user)

### [Chapter 12.03](./chapter12.03/README.md) (Configuring expired session redirect)

### [Chapter 12.04](./chapter12.04/README.md) (Preventing authentication instead of forcing logout)

### [Chapter 12.05](./chapter12.05/README.md) (Other benefits of concurrent session control)

---

## Resources
> * [Project Resources](../docs/resources.md)

---

# [../](../README.md)
