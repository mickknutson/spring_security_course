#!/bin/sh

##---------------------------------------------------------------------------##
## Creating certificates for Chapter 07
##---------------------------------------------------------------------------##
echo "Setup Properties"

KEYS_DIR="src/main/resources/keys"
LINE="--------------------"

##---------------------------------------------------------------------------##
echo "Remove Keys Directory..."
rm -rf "$KEYS_DIR"

##---------------------------------------------------------------------------##
echo "Create Keys Directory..."
mkdir "$KEYS_DIR"

##---------------------------------------------------------------------------##
echo "$LINE"
echo "Creating a client certificate key pair..."

keytool -genkeypair \
-noprompt \
-alias event_manager_client \
-keyalg RSA \
-validity 365 \
-keystore "$KEYS_DIR/event_manager_clientauth.p12" \
-storetype PKCS12 \
-storepass changeit \
-dname "CN=admin1@baselogic.com, OU=Event Manager, O=BASE Logic, L=Park City, S=Utah, C=US"

# Password: (-keypass)
# changeit
# Re-enter password:
# changeit

# What is your first and last name? (CN)
# admin1@baselogic.com

# What is the name of your organizational unit? (OU)
# Event Manager

# What is the name of your organization? (O)
# BASE Logic, Inc

# What is the name of your City or Locality? (L)
# Park City

# What is the name of your State or Province? (S)
# Utah

# What is the two-letter country code for this unit? (C)
# US


##---------------------------------------------------------------------------##
echo "$LINE"
echo "Export the public key to a standard certificate file"
keytool -exportcert \
-alias event_manager_client \
-keystore "$KEYS_DIR/event_manager_clientauth.p12" \
-storetype PKCS12 \
-storepass changeit \
-file "$KEYS_DIR/event_manager_clientauth.cer"


##---------------------------------------------------------------------------##
echo "$LINE"
echo "import the certificate into the trust store"
keytool -importcert \
-noprompt \
-alias event_manager_client \
-keystore "$KEYS_DIR/event_manager.truststore" \
-storepass changeit \
-file "$KEYS_DIR/event_manager_clientauth.cer"



##---------------------------------------------------------------------------##
echo "$LINE"
echo "...The End..."
echo "$LINE"

##---------------------------------------------------------------------------##