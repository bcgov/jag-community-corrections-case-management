## Build stage
##
FROM maven:3.6.0-jdk-11-slim AS build
COPY ./src /home/app/src
COPY ./pom.xml /home/app
RUN mvn -f /home/app/pom.xml package

FROM azul/zulu-openjdk:11
RUN apt-get update && apt-get install -y \
gnupg2 \
wget \
less \
&& rm -rf /var/lib/apt/lists/*

## Install Chrome
#RUN wget -q -O - https://dl-ssl.google.com/linux/linux_signing_key.pub | apt-key add - && \
#    echo "deb http://dl.google.com/linux/chrome/deb/ stable main" > /etc/apt/sources.list.d/google-chrome.list && \
#    apt-get update && \
#    apt-get install -y google-chrome-stable

# Install Microsoft Edge Stable
RUN wget -qO- https://packages.microsoft.com/keys/microsoft.asc | gpg --dearmor > microsoft.gpg && \
    mv microsoft.gpg /etc/apt/trusted.gpg.d/microsoft.gpg && \
    echo "deb [arch=amd64] https://packages.microsoft.com/repos/edge stable main" > /etc/apt/sources.list.d/microsoft-edge.list && \
    apt-get update && \
    apt-get install -y microsoft-edge-stable


# copy local jar
#COPY ./target/cccm-automation-test-jag-0.0.1-SNAPSHOT-test-jar-with-dependencies.jar .

#copy jar from docker build
COPY --from=build ./home/app/target/cccm-automation-test-ui-0.0.1-SNAPSHOT-test-jar-with-dependencies.jar  .

#EXPOSE 8080
ENTRYPOINT ["java","-Xss2048k","-jar","cccm-automation-test-ui-0.0.1-SNAPSHOT-test-jar-with-dependencies.jar", "eticket=true"]
