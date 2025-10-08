# Use an official lightweight OpenJDK 17 image
FROM eclipse-temurin:17-jdk

# Set the working directory inside the container
WORKDIR /app

# Copy the built JAR file from Gradle output
COPY build/libs/*.jar app.jar

# Expose the default Spring Boot port
EXPOSE 8084

# Run the Spring Boot JAR
ENTRYPOINT ["java", "-jar", "app.jar"]