# Estágio 1: Build da aplicação otimizado para a RAM do Render
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY . .
# Limita o uso de memória do Java/Maven para não estourar o limite de 512MB do Free Tier
ENV MAVEN_OPTS="-Xmx384m"
RUN mvn clean package -DskipTests

# Estágio 2: Execução leve
FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-Xmx384m", "-jar", "app.jar"]