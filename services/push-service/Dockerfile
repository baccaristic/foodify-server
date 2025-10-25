FROM gradle:8.10-jdk17 AS build
WORKDIR /workspace
COPY ../../gradlew ./
COPY ../../gradlew.bat ./
COPY ../../gradle ./gradle
COPY ../../settings.gradle ./
COPY ../../build.gradle ./
COPY ../../services ./services
RUN ./gradlew :services:push-service:bootJar --no-daemon

FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /workspace/services/push-service/build/libs/push-service-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8083
ENTRYPOINT ["java", "-jar", "app.jar"]
