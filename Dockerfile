# Use official OpenJDK as the base image
FROM openjdk:21-jdk-slim as builder

# Set the working directory
WORKDIR /app

# Copy the pom.xml and download dependencies
COPY pom.xml .

# Copy the rest of the application code
COPY src ./src

COPY mvnw .
COPY .mvn .mvn

RUN chmod +x ./mvnw

# Use Maven to build the application (skip tests for faster build)
RUN ./mvnw clean install -DskipTests

# Package the application into a runnable JAR
RUN ./mvnw clean package -DskipTests

# Set the command to run the application
ENTRYPOINT ["java", "-jar", "target/WoC_CRS-0.0.1-SNAPSHOT.jar"]

# Expose the port your application runs on
EXPOSE 8080
