#
# Build stage
#
FROM eclipse-temurin:21-jdk-jammy AS build
ENV HOME=/usr/app/module
ENV DOCKER_BUILDKIT=1
ENV APP=/usr/app
RUN mkdir -p $HOME
WORKDIR $HOME
ADD . $HOME
RUN target=/root/.m2 ./mvnw -f $HOME/pom.xml clean install -DskipTests=true

#
# Package stage
#
FROM eclipse-temurin:21-jre-jammy
ARG JAR_FILE=/usr/app/module/target/graphql-api-0.0.1-SNAPSHOT.jar
COPY --from=build $JAR_FILE /app/module/runner.jar
EXPOSE 8080
ENTRYPOINT java -jar /app/module/runner.jar
