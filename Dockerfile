FROM maven:3.8.6-eclipse-temurin-17 AS builder

WORKDIR /workspace
COPY . .

RUN mvn -N dependency:go-offline -B

RUN mvn -f pom.xml -pl ./config-server clean package -DskipTests
RUN mvn -f pom.xml -pl ./discovery-service clean package -DskipTests
RUN mvn -f pom.xml -pl ./api-gateway clean package -DskipTests
RUN mvn -f pom.xml -pl ./kafka-notification-impl clean package -DskipTests
RUN mvn -f pom.xml -pl ./crud-api clean package -DskipTests

FROM eclipse-temurin:23-jdk as config-server
RUN apt-get update && apt-get install -y curl
COPY --from=builder /workspace/config-server/target/*.jar /app.jar
EXPOSE 8888
ENTRYPOINT ["java", "-jar", "/app.jar"]

FROM eclipse-temurin:23-jdk as discovery-service
COPY --from=builder /workspace/discovery-service/target/*.jar /app.jar
EXPOSE 8085
ENTRYPOINT ["java", "-jar", "/app.jar"]

FROM eclipse-temurin:23-jdk as kafka-notification-impl
COPY --from=builder /workspace/kafka-notification-impl/target/*.jar /app.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "/app.jar"]

FROM eclipse-temurin:23-jdk as api-gateway
COPY --from=builder /workspace/api-gateway/target/*.jar /app.jar
EXPOSE 8084
ENTRYPOINT ["java", "-jar", "/app.jar"]

FROM eclipse-temurin:23-jdk as crud-api
COPY --from=builder /workspace/crud-api/target/*.jar /app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]