# Use official OpenJDK as the base image
FROM openjdk:21-jdk-slim as builder

# Set the working directory
WORKDIR /app

# Copy the pom.xml and download dependencies
COPY pom.xml .

# Use Maven to build the application (skip tests for faster build)
RUN mvn clean install -DskipTests

# Copy the rest of the application code
COPY src ./src

# Package the application into a runnable JAR
RUN mvn clean package -DskipTests

# Set the command to run the application
ENTRYPOINT ["java", "-jar", "target/WoC_CRS-0.0.1-SNAPSHOT.jar"]

# Expose the port your application runs on
EXPOSE 8080
