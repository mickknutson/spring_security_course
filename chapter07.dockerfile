#------------------------------------------------------------------------------#
# Docker Build
# inside ~/chapter07
#------------------------------------------------------------------------------#

# docker build --tag spring_security_course/chapter07:latest -f chapter07.dockerfile .

# Temp Builder Image

#docker pull maven:3.6.3-adoptopenjdk-11
FROM maven:3.6.3-adoptopenjdk-11 as builder
COPY . /app
WORKDIR /app/chapter07
RUN mvn clean verify -e
