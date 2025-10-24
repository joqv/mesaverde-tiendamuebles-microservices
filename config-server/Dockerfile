FROM maven:3.9.9-eclipse-temurin-21 AS builder

WORKDIR /workspace

COPY src ./src
COPY pom.xml ./pom.xml

RUN mvn clean install -DskipTests


FROM eclipse-temurin:21-jre

WORKDIR /app

EXPOSE 8888

COPY --from=builder /workspace/target/*.jar config-server.jar

ENTRYPOINT ["java", "-jar", "config-server.jar"]
