# Use official OpenJDK 17 runtime
FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

COPY target/*.war app.war

EXPOSE 8090

ENTRYPOINT [ "java","-jar","app.war" ]