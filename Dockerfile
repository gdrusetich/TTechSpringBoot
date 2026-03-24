# Paso 1: Construir la aplicación
FROM maven:3.8.5-openjdk-17 AS build
COPY . .
RUN mvn clean package -DskipTests

# Paso 2: Ejecutar la aplicación
FROM openjdk:17.0.1-jdk-slim
COPY --from=build /target/*.jar app.jar

# Render asigna el puerto dinámicamente, esto le dice a Java que lo use
ENTRYPOINT ["java","-Dserver.port=${PORT}","-jar","app.jar"]