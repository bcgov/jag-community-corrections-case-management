#############################################################################################
###              Stage where Docker is building spring boot app using maven               ###
#############################################################################################
FROM maven:3.6.3-jdk-11 as build

ARG SERVICE_NAME
ARG SKIP_TESTS=false

COPY . .

RUN mvn -B clean install \
        -P all \
        -Dmaven.test.skip=${SKIP_TESTS}

#############################################################################################

#############################################################################################
### Stage where Docker is running a java process to run a service built in previous stage ###
#############################################################################################
FROM openjdk:11-jdk-slim

# ARG MVN_PROFILES
ARG SERVICE_NAME

COPY --from=build ./${SERVICE_NAME}/target/${SERVICE_NAME}-*.jar /app/service.jar

CMD ["java", "-jar", "/app/service.jar"]
#############################################################################################
