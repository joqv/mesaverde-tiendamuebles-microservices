FROM maven:3.9.9-eclipse-temurin-21 AS builder

WORKDIR /workspace

COPY common-service-tm/pom.xml ./common-service-tm/
COPY common-service-tm/src ./common-service-tm/src

RUN mvn -f common-service-tm/pom.xml clean install -DskipTests


# COPY spring-error-handler/pom.xml ./spring-error-handler/
# COPY spring-error-handler/src ./spring-error-handler/src

# RUN mvn -f spring-error-handler/pom.xml clean install -DskipTests


COPY auth-service/pom.xml .
COPY auth-service/src ./src

RUN mvn clean install -DskipTests



FROM eclipse-temurin:21-jre

WORKDIR /app

EXPOSE 9000

COPY --from=builder /workspace/target/*.jar auth-service.jar

ENTRYPOINT ["java", "-jar", "auth-service.jar"]