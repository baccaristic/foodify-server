FROM gradle:8.10-jdk17 AS build
WORKDIR /workspace
COPY ../../gradlew ./
COPY ../../gradlew.bat ./
COPY ../../gradle ./gradle
COPY ../../settings.gradle ./
COPY ../../build.gradle ./
COPY ../../services ./services
RUN ./gradlew :services:api-gateway:bootJar --no-daemon

FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /workspace/services/api-gateway/build/libs/api-gateway-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
