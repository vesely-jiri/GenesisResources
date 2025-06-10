FROM maven:3.9.6-eclipse-temurin-21-alpine AS builder

WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jdk-alpine

WORKDIR /app

COPY --from=builder /app/target/backend-*.jar backend.jar

EXPOSE 8080

CMD ["java", "-jar", "backend.jar"]