# Этап сборки
FROM maven:3.8.4-openjdk-8 AS builder

WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Этап исполнения (без Alpine)
FROM eclipse-temurin:8-jdk

WORKDIR /app
COPY --from=builder /app/target/*.jar /app/app.jar

EXPOSE 8181
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
