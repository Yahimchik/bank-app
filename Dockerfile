FROM maven:latest AS build

WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline -B

COPY src ./src

RUN mvn clean package -DskipTests

FROM openjdk:17-jdk-slim

WORKDIR /app

COPY --from=build /app/target/*.jar /app/app.jar

EXPOSE 9080

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
