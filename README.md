# Kafka UI

Basic UI for consuming Kafka topics easily.

It also works as a example for Kotlin, Vaadin and Spring Boot.

## Build

The project use [Maven](https://maven.apache.org/) to build, you can compile it using the following command:

```
mvn clean install
```
 
It will generate a WAR file in the target folder **target/kafka-ui.war**.

## Tests

To execute all the tests in the project, you can use the next command:

```
mvn test
```
 
## Configuration
 
The service can be configured through the following environment variables:
 
*   SERVER_PORT (9000): define the port to expose the endpoints
 
Or changing directly the property file **src\main\resources\application.properties**.

## Run

After the build, the project generate a runnable WAR that can be executed as a regular jar:

```
java -jar target/kafka-ui.war
```

In development you can also use the Spring Boot Maven plugin as follow:

```
mvn spring-boot:run
```

Or directly in the IDE execute the class **TrackerApiApplication**.

## Docker

### Docker Build

Alternatively the project support [Docker](https://www.docker.com/), you can execute the above command to compile the repo and build the 
Docker image:

```
docker build -t kafka-ui .
```

### Docker Config

Ports:
* **9000** to expose Restful endpoints.

### Docker Run

After building the docker image you can also run the service using docker with the 
following command:

```
docker run -p 9000:9000 --name kafka-ui kafka-ui
```

To configure the image you need to define one of the env variable from the **Configuration** 
section, for example to use a custom port you can use the following command:

```
docker run -p 8000:9000 -e "SERVER_PORT=8000" --name kafka-ui kafka-ui
```

## Known Issues

*   No known issues registered yet.

## Useful Links

*	Spring Boot docs: https://docs.spring.io/spring-boot/docs/2.0.0.M6/reference/htmlsingle/