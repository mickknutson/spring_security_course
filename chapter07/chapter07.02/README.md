# Chapter 07.02

## Advanced Authentication

While a secure connection is established, the client verifies the server according to its 
certificate (issued by a trusted certificate authority).

Beyond that, X.509 support in Spring Security can be used to verify the identity of a 
client by the server while connecting. 
This is called “mutual authentication”, and we'll look at how that's done here as well.


TODO: REWORD:
Mutual Authentication
In the previous section, we presented how to implement the most common SSL authentication schema – server-side authentication. This means, only a server authenticated itself to clients.

In this section, we'll describe how to add the other part of the authentication – client-side authentication. This way, only clients with valid certificates signed by the authority that our server trusts, can access our secured website.

But before we continue, let's see what are the pros and cons of using the mutual SSL authentication.

Pros:

> * The private key of an X.509 client certificate is stronger than any user-defined password. But it has to be kept secret!
> * With a certificate, the identity of a client is well-known and easy to verify.
> * No more forgotten passwords!

Cons:

> * We need to create a certificate for each new client.
> * The client's certificate has to be installed in a client application. In fact: X.509 client authentication is device-dependent, which makes it impossible to use this kind of authentication in public areas, for example in an internet-café.
> * There must be a mechanism to revoke compromised client certificates.
> * We must maintain the clients' certificates. This can easily become costly.

## Tasks

### Creating a client certificate key pair

Now, I have made a set of scripts to automate the certificate generation process

    ./certificateGenerator.sh

This will create a client certificate pair and import the private certificate into a tomcat.keystore with the validity of the certificate 365 days from the date the certificate is generated.
By running the above script, the existing certificates will be deleted, and regenerated and put in:

    ./target/classes/keys/**


### Configuring Tomcat in Spring Boot

application.yml:

    server:
      port: 8443
      ssl:
        key-store: "classpath:keys/event_manager_clientauth.p12"
        key-store-password: changeit
        key-store-type: PKCS12
        key-alias: event_manager_client
    
        protocol: TLS
        enabled: true
    
        trust-store: "classpath:keys/event_manager.truststore"
        trust-store-password: changeit
        
        # Whether client authentication is not wanted ("none"), wanted ("want") or needed ("need")
        client-auth: need


### Importing the certificate key pair into a browser
#### Using Firefox
#### Using Chrome


### Configuring client certificate authentication in Spring Security
...


---

# [../](../README.md)
