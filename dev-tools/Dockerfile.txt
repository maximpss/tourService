FROM maven:3.9.16-eclipse-temurin-25-alpine AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

FROM eclipse-temurin:25-jre-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar tour-service.jar
ENTRYPOINT ["java", "-jar", "tour-service.jar"]