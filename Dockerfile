#BUILD PROJECT STAGE
FROM maven:3.6.0-jdk-11-slim as builder
ADD pom.xml pom.xml
RUN mvn verify --fail-never
ADD src src
RUN mvn clean install

#SETUP ENV STAGE
FROM openjdk:11-jdk

COPY --from=builder target/kafka-ui.war app.jar

VOLUME /tmp

ENV JAVA_OPTS=""
ENV SERVER_PORT=9000

EXPOSE 9000

ENTRYPOINT exec java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /app.jar