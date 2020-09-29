# Chapter 07.01

## Advanced Authentication

While a secure connection is established, the client verifies the server according to its 
certificate (issued by a trusted certificate authority).

But beyond that, X.509 in Spring Security can be used to verify the identity of a 
client by the server while connecting. 
This is called “mutual authentication”, and we'll look at how that's done here as well.

## Tasks

### Creating a certificate key pair

Now, I have made a set of scripts to automate the certificate generation process

    ./runTrustStoreGenerator.sh

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


### Verifying the certificate key pair into a browser

#### Using Firefox

### Allow insecure localhost connections on Chrome:

> * [Read: getting-chrome-to-accept-self-signed-localhost-certificate](https://stackoverflow.com/questions/7580508/getting-chrome-to-accept-self-signed-localhost-certificate)
> * [Enable Flag: chrome://flags/#allow-insecure-localhost](chrome://flags/#allow-insecure-localhost)


## Optional Configuration:

### Install certbot

> * [Certbot (OSX)](https://certbot.eff.org/lets-encrypt/osx-apache)
brew install certbot

> * Create cert on command line

...


https://docs.spring.io/spring-boot/docs/current/reference/html/howto.html#howto-enable-multiple-connectors-in-tomcat
---

# [../](../)
