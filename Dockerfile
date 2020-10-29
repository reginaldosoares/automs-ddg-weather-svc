# Use the official maven/Java 8 image to create a build artifact.
# https://hub.docker.com/_/maven
FROM openjdk:8-jdk-alpine AS builder

# Set the working directory to /app
WORKDIR /app

# copy local to build container
COPY . ./

# Download dependencies and build a release artifact.
RUN ./gradlew clean build --exclude-task test

# Use AdoptOpenJDK for base image.
# It's important to use OpenJDK 8u191 or above that has container support enabled.
# https://hub.docker.com/r/adoptopenjdk/openjdk8
# https://docs.docker.com/develop/develop-images/multistage-build/#use-multi-stage-builds
FROM adoptopenjdk/openjdk8:alpine-slim

RUN mkdir -p app/automation

# Copy the jar to the production image from the builder stage.
COPY --from=builder /app/build/libs/automs-ddg-weather-svc-*.jar /app/automation/automs-ddg-weather-svc.jar

# Run the web service on container startup.
ENTRYPOINT ["java", "-jar", "/app/automation/automs-ddg-weather-svc.jar"]
