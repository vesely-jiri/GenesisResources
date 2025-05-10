FROM openjdk:17-jdk-alpine

WORKDIR /app

COPY target/backend-*.jar backend.jar

EXPOSE 8080

CMD ["java", "-jar", "backend.jar"]