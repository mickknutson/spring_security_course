#!/bin/sh

##---------------------------------------------------------------------------##
## Creating certificates for Chapter 07
## ./runTrustStoreGenerator.sh
##---------------------------------------------------------------------------##
echo "--> Setup Properties"

KEYS_DIR="src/main/resources/keys"
ALIAS="event_manager_client"
KEYSTORE="event_manager_clientauth.p12"
CERTIFICATE="event_manager_clientauth.cer"
TRUSTSTORE="event_manager.truststore"

PASSWORD="changeit"
DNAME="CN=admin1@baselogic.com, OU=Event Manager, O=BASE Logic, L=Park City, S=Utah, C=US"
SAN="SAN=IP:IP:127.0.0.1,DNS:baselogic.com,DNS:localhost,EMAIL:user1@baselogic.com,EMAIL:admin1@baselogic.com"
LINE="--------------------"
STARS="********************"


##---------------------------------------------------------------------------##
echo "--> Remove Keys Directory..."
rm -rf "$KEYS_DIR"

##---------------------------------------------------------------------------##
echo "--> Create Keys Directory..."
mkdir "$KEYS_DIR"

##---------------------------------------------------------------------------##
echo "$LINE"
echo "--> Creating a client certificate key pair..."

keytool -genkeypair \
-noprompt \
-alias "$ALIAS" \
-keyalg RSA \
-keysize 2048 \
-validity 365 \
-keystore "$KEYS_DIR/$KEYSTORE" \
-storetype PKCS12 \
-storepass "$PASSWORD" \
-dname "$DNAME" \
#-ext "$SAN"


##---------------------------------------------------------------------------##
echo "$LINE"
echo "--> Export the public key to a standard certificate file"
keytool -exportcert \
-alias "$ALIAS" \
-keystore "$KEYS_DIR/$KEYSTORE" \
-storetype PKCS12 \
-storepass "$PASSWORD" \
-file "$KEYS_DIR/$CERTIFICATE"


##---------------------------------------------------------------------------##
echo "$LINE"
echo "--> Import the certificate into the trust store"
keytool -importcert \
-noprompt \
-alias event_manager_client \
-keystore "$KEYS_DIR/$TRUSTSTORE" \
-storepass "$PASSWORD" \
-file "$KEYS_DIR/$CERTIFICATE"

##---------------------------------------------------------------------------##
echo "$LINE"
echo "--> List certificates in the $TRUSTSTORE"

keytool -list -v -keystore "$KEYS_DIR/$TRUSTSTORE" -storepass "$PASSWORD"


##---------------------------------------------------------------------------##
echo "$LINE"
echo "...The End..."
echo "$LINE"

##---------------------------------------------------------------------------##
