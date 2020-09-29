#!/bin/sh

##---------------------------------------------------------------------------##
## Creating certificates for Chapter 07
## ./runCertificateGenerator.sh
##---------------------------------------------------------------------------##
echo "Setup Properties"

KEYS_DIR="src/main/resources/keys"
PASSWORD="changeit"
DNAME="CN=admin1@baselogic.com, OU=Event Manager, O=BASE Logic, L=Park City, S=Utah, C=US"
LINE="--------------------"
STARS="********************"

##---------------------------------------------------------------------------##
echo "Remove Keys Directory..."
rm -rf "$KEYS_DIR"

##---------------------------------------------------------------------------##
echo "Create Keys Directory..."
mkdir "$KEYS_DIR"


##---------------------------------------------------------------------------##
echo "RUN mkcert"

sudo mkcert \
-cert-file "$KEYS_DIR/baselogicCert.pem" \
-key-file "$KEYS_DIR/baselogicKey.pem" \
-p12-file "$KEYS_DIR/event_manager_client.p12" \
-pkcs12 \
baselogic.com '*.baselogic.com' localhost 127.0.0.1 ::1


KEYS_DIR="src/main/resources/keys"
sudo chown -R mickknutson:staff "$KEYS_DIR"
sudo chmod -R 777 "$KEYS_DIR"

keytool -printcert -file "$KEYS_DIR/event_manager_client.p12"

##---------------------------------------------------------------------------##
#echo "Create P12"
#
#openssl pkcs12 -export -out "$KEYS_DIR/event_manager_clientauth.p12" \
#-in "$KEYS_DIR/baselogicCert.pem" \
#-inkey "$KEYS_DIR/baselogicKey.pem" \
##-passout "pass: $PASSWORD"


##---------------------------------------------------------------------------##
echo "$LINE"
echo "--> Export the public key to a standard certificate file"
keytool -exportcert \
-file "$KEYS_DIR/event_manager_client.cer" \
-alias event_manager_client \
-keystore "$KEYS_DIR/event_manager_client.p12" \
-storetype PKCS12 \
-storepass "$PASSWORD" \


##---------------------------------------------------------------------------##
echo "$LINE"
echo "--> Import the certificate into the trust store"
keytool -importcert \
-noprompt \
-alias event_manager_client \
-keystore "$KEYS_DIR/event_manager.truststore" \
-storepass "$PASSWORD" \
-file "$KEYS_DIR/event_manager_client.cer"



##---------------------------------------------------------------------------##
echo "$LINE"
echo "...The End..."
echo "$LINE"

##---------------------------------------------------------------------------##