#!/bin/sh

##---------------------------------------------------------------------------##
## Creating certificates for Chapter 07
## runCertbotGenerator.sh
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
echo "Execute certbot ... --standalone "

sudo certbot certonly \
--domains="www.baselogic.com" \
--webroot -w="$KEYS_DIR"
--rsa-key-size 4096

##---------------------------------------------------------------------------##
echo "$LINE"
echo "...The End..."
echo "$LINE"

##---------------------------------------------------------------------------##