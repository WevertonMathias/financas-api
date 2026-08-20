# Estágio 1: Build com Java 21
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY . .

ENV MAVEN_OPTS="-Xmx384m"
RUN mvn clean package -DskipTests

# Estágio 2: Execução com Java 21
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-Xmx384m", "-jar", "app.jar"]