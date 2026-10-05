# Крок 1: Збірка проекту через Maven
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Крок 2: Запуск готового JAR із лімітом пам'яті для Free Tier
FROM eclipse-temurin-21-jre
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
ENTRYPOINT ["java", "-Xmx384m", "-jar", "app.jar"]