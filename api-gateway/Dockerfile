# Stage 1: Use Maven for building
FROM maven:3.9.9-sapmachine-17 AS build
LABEL authors="ctv"
WORKDIR /app
COPY ./api-gateway/ ./gateway
WORKDIR /app/gateway
RUN mvn clean package

# Stage 2: Use OpenJDK for running
FROM openjdk:24-slim-bullseye
WORKDIR /app
COPY --from=build /app/gateway/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]

# Build command: docker build -t gateway_image -f api-gateway/Dockerfile .
# Run command: docker run --name gateway_container -p 8080:8080 gateway_image