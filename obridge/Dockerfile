
#############################################################################################

### Stage where Docker is running a java process to run a service built in previous stage ###

#############################################################################################

FROM eclipse-temurin:21-jre-jammy

RUN apt-get update

# ARG MVN_PROFILES

ARG SERVICE_NAME=cccm-data-obridge
ARG ORACLE_HOST= ${ORACLE_HOST}
ARG ORACLE_SID= ${ORACLE_SID}
ARG ORACLE_USERNAME= ${ORACLE_USERNAME}
ARG ORACLE_PASSWORD= ${ORACLE_PASSWORD}

COPY *.jar app.jar
COPY obridge/docker-entrypoint.sh /
RUN chmod +x /docker-entrypoint.sh

CMD ["/docker-entrypoint.sh"]

#############################################################################################