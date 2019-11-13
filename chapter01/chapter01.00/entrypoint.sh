#!/bin/sh

# TODO: Need to pass in server variable:
while ! nc -z configserver 8888 ; do
    echo "Waiting for upcoming Config Server"
    sleep 2
done

java -Djava.security.egd=file:/dev/./urandom -jar ./application-exec.jar
