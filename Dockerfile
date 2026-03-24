# Paso 1: Construir la aplicación (Cambiamos 17 por 21)
FROM maven:3.9.6-eclipse-temurin-21 AS build
COPY . .
RUN mvn clean package -DskipTests

# Paso 2: Ejecutar la aplicación (Cambiamos 17 por 21)
FROM eclipse-temurin:21-jre-jammy
COPY --from=build /target/*.jar app.jar

# Render asigna el puerto dinámicamente
ENTRYPOINT ["java","-Dserver.port=${PORT}","-jar","app.jar"]