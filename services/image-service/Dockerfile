FROM gradle:8.10-jdk17 AS build
WORKDIR /workspace
COPY ../../gradlew ./
COPY ../../gradlew.bat ./
COPY ../../gradle ./gradle
COPY ../../settings.gradle ./
COPY ../../build.gradle ./
COPY ../../services ./services
RUN ./gradlew :services:image-service:bootJar --no-daemon

FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /workspace/services/image-service/build/libs/image-service-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8082
ENTRYPOINT ["java", "-jar", "app.jar"]
