# Chapter 07.01
## Advanced Authentication
This chapter is the chapter BASE and continues from the previous
chapter section, from here, we build on top of this base.

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
        client-auth: need
    
            port: 8443
            ssl:
                key-store: "classpath:keys/event_manager_clientauth.p12"
                key-store-password: changeit
                keyStoreType: PKCS12
                keyAlias: event_manager_client
                protocol: TLS


### Importing the certificate key pair into a browser
#### Using Firefox
#### Using Chrome


### Configuring client certificate authentication in Spring Security
...


---

# [../](../)
