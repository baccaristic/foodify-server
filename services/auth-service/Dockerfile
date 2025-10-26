FROM gradle:8.10-jdk17 AS build
WORKDIR /workspace

COPY ../../gradlew ./
COPY ../../gradlew.bat ./
COPY ../../gradle ./gradle
COPY ../../settings.gradle ./
COPY ../../build.gradle ./
COPY ../../services ./services

RUN ./gradlew :services:auth-service:bootJar --no-daemon

FROM eclipse-temurin:17-jre
WORKDIR /app

COPY --from=build /workspace/services/auth-service/build/libs/*.jar app.jar

ENTRYPOINT ["java","-jar","/app/app.jar"]
