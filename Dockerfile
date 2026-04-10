# Use official OpenJDK 17 runtime
FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

COPY target/Docker-User-Service-Docker-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT [ "java","-jar","app.jar" ]