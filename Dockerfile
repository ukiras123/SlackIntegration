FROM openjdk:8

# Install Maven
RUN apt-get update
RUN apt-get install -y maven

WORKDIR /code

# Prepare by downloading dependencies
ADD pom.xml /code/pom.xml
ADD src /code/src

RUN ["mvn", "clean", "install"]

# Adding source, compile and package into a fat jar
RUN ["mvn", "package"]

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "target/slack-integration.jar"]