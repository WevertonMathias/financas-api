# Estágio 1: Build
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY . .

# Concede permissão ao wrapper e executa o build limitando o uso de memória RAM
RUN chmod +x mvnw || true
ENV MAVEN_OPTS="-Xmx384m"
RUN mvn clean package -DskipTests -e

# Estágio 2: Execução
FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-Xmx384m", "-jar", "app.jar"]