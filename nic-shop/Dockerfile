FROM maven:3.8.4-openjdk-17 as builder
WORKDIR /nic-shop
COPY . /nic-shop/.
RUN mvn -f /nic-shop/pom.xml clean package -Dmaven.test.skip=true

FROM eclipse-temurin:23-jdk-alpine
WORKDIR /nic-shop
COPY --from=builder /nic-shop/target/*.jar /nic-shop/*.jar
EXPOSE 8181
ENTRYPOINT ["java", "-jar", "/nic-shop/*.jar"]
