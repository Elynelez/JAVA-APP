# Etapa 1: Construcción
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Etapa 2: Ejecución
FROM eclipse-temurin:17-jdk
WORKDIR /app
COPY --from=build /app/target/miapp-0.0.1-SNAPSHOT.jar app.jar

# Render expone el puerto que define $PORT
EXPOSE 8080
CMD ["java", "-jar", "app.jar"]

