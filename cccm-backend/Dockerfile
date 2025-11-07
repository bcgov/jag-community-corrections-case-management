#############################################################################################
###              Stage where Docker is building spring boot app using maven               ###
#############################################################################################
FROM maven:3.8.3-openjdk-17 AS build

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
FROM eclipse-temurin:17-jre-alpine

# ARG MVN_PROFILES
ARG SERVICE_NAME

COPY --from=build ./${SERVICE_NAME}/target/quarkus-app/lib/ /deployments/lib/
COPY --from=build ./${SERVICE_NAME}/target/quarkus-app/*.jar /deployments/service.jar
COPY --from=build ./${SERVICE_NAME}/target/quarkus-app/app/ /deployments/app/
COPY --from=build ./${SERVICE_NAME}/target/quarkus-app/quarkus/ /deployments/quarkus/

CMD ["/entrypoint.sh"]
#############################################################################################
